package io.github.t45k.lvmutator

import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.jdt.core.dom.AST
import org.eclipse.jdt.core.dom.ASTNode
import org.eclipse.jdt.core.dom.ASTParser
import org.eclipse.jdt.core.dom.ASTParser.K_CLASS_BODY_DECLARATIONS
import org.eclipse.jdt.core.dom.ASTParser.K_STATEMENTS
import org.eclipse.jdt.core.dom.ASTVisitor
import org.eclipse.jdt.core.dom.AssertStatement
import org.eclipse.jdt.core.dom.Block
import org.eclipse.jdt.core.dom.BreakStatement
import org.eclipse.jdt.core.dom.ContinueStatement
import org.eclipse.jdt.core.dom.DoStatement
import org.eclipse.jdt.core.dom.EmptyStatement
import org.eclipse.jdt.core.dom.EnhancedForStatement
import org.eclipse.jdt.core.dom.ExpressionStatement
import org.eclipse.jdt.core.dom.ForStatement
import org.eclipse.jdt.core.dom.IfStatement
import org.eclipse.jdt.core.dom.LabeledStatement
import org.eclipse.jdt.core.dom.ReturnStatement
import org.eclipse.jdt.core.dom.Statement
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor
import org.eclipse.jdt.core.dom.SwitchStatement
import org.eclipse.jdt.core.dom.SynchronizedStatement
import org.eclipse.jdt.core.dom.ThrowStatement
import org.eclipse.jdt.core.dom.TryStatement
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.jdt.core.dom.TypeDeclarationStatement
import org.eclipse.jdt.core.dom.VariableDeclarationStatement
import org.eclipse.jdt.core.dom.WhileStatement
import org.eclipse.jdt.core.dom.YieldStatement
import java.io.File
import kotlin.random.Random


class Mutator {
    private val rand: Random = Random(System.currentTimeMillis())

    fun mutate() {
        val lines: MutableList<String> = mutableListOf()
        // These statements comes from original Mutation Injection Framework.
        // see https://github.com/jeffsvajlenko/MutationInjectionFramework/blob/master/customMutationOperators/GapClones/src/Mutator.java
        lines.add("impl.addParameter(objectFlowState, parameter);")
        lines.add("return FunctionUtils.toDifferentiableUnivariateFunction(this).derivative();")
        lines.add("TestConstructor tc = new TestConstructor(\"saaa\");")
        lines.add("StreamResult sr = new StreamResult();")
        lines.add("if(DEBUG)System.out.println(\"reusing instance, object id : \" + fStreamWriter);")
        lines.add("g.fillOval(r.x, r.y, r.width-1, r.height-1);")
        lines.add("dummyAction.removePropertyChangeListener(arg0);")
        lines.add("throw new AssertionError(\"Trees.getOriginalType() error!\");")
        lines.add("oidStore.put(s.getName(), s) ;")
        lines.add("String sTypeVS = (String) lst.getSelectedItem();")
        lines.add("coords[nc++] = (x + ctrls[i + 0] * w + ctrls[i + 1] * aw);")
        lines.add("throw new UniqueFieldValueConstraintViolationException(classMetadata().getName(), fieldMetadata().getName());")
        lines.add("HardObjectReference ref = HardObjectReference.peekPersisted(trans, id, 1);")
        lines.add("org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [0];")
        lines.add("assertEquals(\"true\", buildRule.getProject().getProperty(\"error\"));")
        lines.add("block[block.length - 2]  = (byte)(len >> 8);")
        lines.add("factories.add(new PrintStreamProviderFactory(ps));")
        lines.add("column = (column / TabInc * TabInc) + TabInc;")
        lines.add("int lineStart = startPosition[getLineNumber(pos) - FIRSTLINE];")
        lines.add("((ComponentUI) (uis.elementAt(0))).getAccessibleChildrenCount(a);")

        val statements: List<Statement> = lines.map {
            ASTParser.newParser(AST.JLS14)
                .apply { setSource(it.toCharArray()) }
                .apply { setKind(K_STATEMENTS) }
                .let { it.createAST(NullProgressMonitor()) as Block }
                .statements()[0] as Statement
        }

        var fileIndex = 1
        for (i in 1..100) {
            val fragmentFile = File("fragment", "$i")
            val methodDeclaration = ASTParser.newParser(AST.JLS14)
                .apply { setSource(fragmentFile.readText().toCharArray()) }
                .apply { setKind(K_CLASS_BODY_DECLARATIONS) }
                .let { it.createAST(NullProgressMonitor()) as TypeDeclaration }
                .methods[0]

            for (insertion in statements.shuffled(rand)) {
                val visitor = StatementVisitor()
                methodDeclaration.accept(visitor)
                val target: Statement = visitor.getStatements().shuffled(rand).first { it.parent is Block }
                val oldBlock: Block = target.parent as Block
                val beforeOrAfter = if (rand.nextBoolean()) 0 else 1
                val insertedStatement = ASTNode.copySubtree(target.ast, insertion) as Statement
                oldBlock.statements().add(oldBlock.statements().indexOf(target) + beforeOrAfter, insertedStatement)
                File("mutantfragment", "${fileIndex++}").writeText(methodDeclaration.toString())
            }
        }
    }

    class StatementVisitor : ASTVisitor() {
        private val statements: MutableList<Statement> = mutableListOf()

        fun getStatements(): List<Statement> = statements

        override fun visit(node: AssertStatement?): Boolean {
            if (node != null) {
                statements.add(node)
            }
            return super.visit(node)
        }

        override fun visit(node: BreakStatement?): Boolean {
            if (node != null) {
                statements.add(node)
            }
            return super.visit(node)
        }

        override fun visit(node: ContinueStatement?): Boolean {
            if (node != null) {
                statements.add(node)
            }
            return super.visit(node)
        }

        override fun visit(node: DoStatement?): Boolean {
            if (node != null) {
                statements.add(node)
            }
            return super.visit(node)
        }

        override fun visit(node: EmptyStatement?): Boolean {
            if (node != null) {
                statements.add(node)
            }
            return super.visit(node)
        }

        override fun visit(node: EnhancedForStatement?): Boolean {
            if (node != null) {
                statements.add(node)
            }
            return super.visit(node)
        }

        override fun visit(node: ExpressionStatement?): Boolean {
            if (node != null) {
                statements.add(node)
            }
            return super.visit(node)
        }

        override fun visit(node: ForStatement?): Boolean {
            if (node != null) {
                statements.add(node)
            }
            return super.visit(node)
        }

        override fun visit(node: IfStatement?): Boolean {
            if (node != null) {
                statements.add(node)
            }
            return super.visit(node)
        }

        override fun visit(node: LabeledStatement?): Boolean {
            if (node != null) {
                statements.add(node)
            }
            return super.visit(node)
        }

        override fun visit(node: ReturnStatement?): Boolean {
            if (node != null) {
                statements.add(node)
            }
            return super.visit(node)
        }

        override fun visit(node: SwitchStatement?): Boolean {
            if (node != null) {
                statements.add(node)
            }
            return super.visit(node)
        }

        override fun visit(node: SynchronizedStatement?): Boolean {
            if (node != null) {
                statements.add(node)
            }
            return super.visit(node)
        }

        override fun visit(node: ThrowStatement?): Boolean {
            if (node != null) {
                statements.add(node)
            }
            return super.visit(node)
        }

        override fun visit(node: TryStatement?): Boolean {
            if (node != null) {
                statements.add(node)
            }
            return super.visit(node)
        }

        override fun visit(node: TypeDeclarationStatement?): Boolean {
            if (node != null) {
                statements.add(node)
            }
            return super.visit(node)
        }

        override fun visit(node: VariableDeclarationStatement?): Boolean {
            if (node != null) {
                statements.add(node)
            }
            return super.visit(node)
        }

        override fun visit(node: WhileStatement?): Boolean {
            if (node != null) {
                statements.add(node)
            }
            return super.visit(node)
        }

        override fun visit(node: YieldStatement?): Boolean {
            if (node != null) {
                statements.add(node)
            }
            return super.visit(node)
        }
    }
}