// Tree Node will contains
class TreeNode {
  private val ASCII_CHARACTER_LIMIT: Int = 128
  var isWord: Boolean = false
  val children: Array[Option[TreeNode]] = (for (_ <- 0 until ASCII_CHARACTER_LIMIT) yield None).toArray
}
