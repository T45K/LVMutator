package io.github.t45k.lvmutator

import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.jdt.core.dom.AST.JLS14
import org.eclipse.jdt.core.dom.ASTNode
import org.eclipse.jdt.core.dom.ASTParser
import org.eclipse.jdt.core.dom.ASTVisitor
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor
import org.eclipse.jdt.core.dom.CompilationUnit
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.SimplePropertyDescriptor
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor
import java.io.File
import kotlin.random.Random

class Collector {
    companion object {
        private const val MIN = 15
        private const val MAX = 50
    }

    private val rand: Random = Random(System.currentTimeMillis())

    fun collect(dirName: String) {
        File(dirName).walk()
            .filter { it.toString().endsWith(".java") }
            .filterNot { it.toString().contains("src/test") }
            .flatMap { javaFile ->
                val compilationUnit = ASTParser.newParser(JLS14)
                    .also { it.setSource(javaFile.readText().toCharArray()) }
                    .let { it.createAST(NullProgressMonitor()) as CompilationUnit }
                val visitor = MethodCollector(compilationUnit, javaFile)
                compilationUnit.accept(visitor)
                visitor.getMethods()
            }
            .shuffled(rand)
            .take(100)
            .forEachIndexed { index, method ->
                File("fragment", "${index + 1}").writeText(method)
            }
    }

    class MethodCollector(private val compilationUnit: CompilationUnit, private val javaFile: File) : ASTVisitor() {
        private val methods: MutableList<String> = mutableListOf()
        fun getMethods(): List<String> = methods

        override fun visit(node: MethodDeclaration?): Boolean {
            node?.also {
                val startLine = if (node.javadoc == null) {
                    compilationUnit.getLineNumber(node.startPosition)
                } else {
                    compilationUnit.getLineNumber(getNodeNextToJavaDoc(node).startPosition)
                }
                val endLine = compilationUnit.getLineNumber(it.startPosition + it.length)
                val loc = endLine - startLine + 1
                if (loc in MIN..MAX) {
                    node.javadoc = null
                    methods.add(javaFile.readLines().subList(startLine - 1, endLine).joinToString("\n"))
                }
            }
            return false
        }

        @Suppress("UNCHECKED_CAST")
        private fun getNodeNextToJavaDoc(node: MethodDeclaration): ASTNode =
            (node.structuralPropertiesForType() as List<StructuralPropertyDescriptor>)
                .asSequence()
                .drop(1)
                .flatMap {
                    when (it) {
                        is ChildListPropertyDescriptor -> (node.getStructuralProperty(it) as List<ASTNode>).asSequence()
                        is SimplePropertyDescriptor -> emptySequence()
                        else -> sequenceOf(node.getStructuralProperty(it) as ASTNode?)
                    }
                }
                .filterNotNull()
                .first()
    }
}
