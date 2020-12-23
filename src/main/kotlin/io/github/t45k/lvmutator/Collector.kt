package io.github.t45k.lvmutator

import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.jdt.core.dom.AST.JLS14
import org.eclipse.jdt.core.dom.ASTParser
import org.eclipse.jdt.core.dom.ASTVisitor
import org.eclipse.jdt.core.dom.CompilationUnit
import org.eclipse.jdt.core.dom.MethodDeclaration
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
                val visitor = MethodCollector()
                compilationUnit.accept(visitor)
                visitor.getMethods()
            }
            .shuffled(rand)
            .take(100)
            .forEachIndexed { index, methodDeclaration ->
                File("fragment", "${index + 1}").writeText(methodDeclaration.toString())
            }
    }

    class MethodCollector : ASTVisitor() {
        private val methods: MutableList<MethodDeclaration> = mutableListOf()
        fun getMethods(): List<MethodDeclaration> = methods

        override fun visit(node: MethodDeclaration?): Boolean {
            node?.body?.also {
                /*
                val startLine = compilationUnit.getLineNumber(it.startPosition)
                val endLine = compilationUnit.getLineNumber(it.startPosition + it.length)
                val loc = endLine - startLine + 1
                if (loc in MIN..MAX) {
                    node.javadoc = null
                    methods.add(node)
                }
                 */
                if (it.toString().split("\n").size in MIN..MAX) {
                    node.javadoc = null
                    methods.add(node)
                }
            }
            return false
        }
    }
}
