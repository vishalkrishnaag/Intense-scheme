import org.intense.SymbolTable
import org.intense.ast.ASTNode
import org.intense.ast.Type

class PackageNode : ASTNode(){
    lateinit var __package__:String
    override fun inferType(env: SymbolTable): Type {
        TODO("Not yet implemented")
    }

    override fun toKotlinCode(env: SymbolTable): String {
        TODO("Not yet implemented")
    }

    override fun eval(env: SymbolTable): String {
        TODO("Not yet implemented")
    }

}
