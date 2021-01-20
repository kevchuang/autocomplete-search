import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer

object AutocompleteSearch {

  private val ASCII_UPPER_CASE_MIN: Int = 65
  private val ASCII_UPPER_CASE_MAX: Int = 90
  private val ASCII_LOWER_UPPER_RANGE: Int = 32

  /**
   * Add word in a given tree
   * @param treeNode current treeNode
   * @param word a string
   * @param index position of the index incremented at each recursive call
   * @return treeNode
   */
  @tailrec
  def addWord(treeNode: TreeNode, word: String, index: Int): TreeNode = {
    // return treeNode when position of the index equals to the word's length
    if (word.length == index) {
      treeNode.isWord = true
      treeNode
    } else {
      // handling case sensitive by transforming the character when it is in uppercase
      val position: Int = word(index).toInt
      val handleCaseSensitive = if (position >= ASCII_UPPER_CASE_MIN && position <= ASCII_UPPER_CASE_MAX) position + ASCII_LOWER_UPPER_RANGE else position
      if (treeNode.children(handleCaseSensitive).isEmpty)
        treeNode.children(handleCaseSensitive) = Some(new TreeNode)
      // recursive call with the new treeNode and the next position of the index given as a parameter
      addWord(treeNode.children(handleCaseSensitive).get, word, index + 1)
    }
  }

  /**
   * Add words in a given tree
   * @param treeNode treeNode that will contains all the words
   * @param words sequence of string containing the words to add
   * @return the words in a treeNode
   */
  def addWords(treeNode: TreeNode, words: Seq[String]): TreeNode = {
    words.foreach(word => addWord(treeNode, word, index = 0))
    treeNode
  }

  /**
   * Find the node of keyword's last character
   * @param treeNode treeNode that contains all the words
   * @param keyword a string
   * @return the node of keyword's last character
   **/
  @tailrec
  def findStartingNode(treeNode: Option[TreeNode], keyword: String): Option[TreeNode] = {
    // return the node when it is the last character of keyword
    if (keyword.length <= 1) {
      // using headOption to avoid error exception when keyword is empty
      keyword.headOption.flatMap(head => treeNode.flatMap(_.children(head)))
    } else {
      // recursive call with the next node and the keyword.tail given as a parameter
      findStartingNode(treeNode.flatMap(_.children(keyword.head)), keyword.tail)
    }
  }

  /**
   * Find suggested words in a given tree
   * @param treeNode treeNode that contains all the words
   * @param word string to add in the buffer
   * @param suggestedWords array buffer that will contains all the suggested words
   * @return sequence of suggested words
   */
  def findWords(treeNode: TreeNode, word: String, suggestedWords: ArrayBuffer[String]): Seq[String] = {
    (suggestedWords.length match {
        // return the buffer when there are already 4 suggestions
      case l if l == 4 => suggestedWords
      case _ =>
        // add the current word to the buffer when it is a word
        if (treeNode.isWord)
          suggestedWords += word
        else {
          // loop on children nodes to check every character nodes
          for (i <- treeNode.children.indices) {
            // when a children node is defined, find the next node and add the current character to the suggested word
            if (treeNode.children(i).isDefined)
              findWords(treeNode.children(i).get, word :+ i.toChar, suggestedWords)
          }
          suggestedWords
        }
    }).toSeq
  }

  /**
   * search for 4 suggested words from the given sequence, starting with the letters typed
   * @param words sequence that contains all the words
   * @param keyword a string
   * @return the suggested words
   */
  def search(words: Seq[String], keyword: String): Seq[String] = {
    val tree: TreeNode = addWords(new TreeNode, words)
    val wordsBuffer: ArrayBuffer[String] = ArrayBuffer.empty[String]

    findStartingNode(Some(tree), keyword.toLowerCase)
      .map(findWords(_, keyword, wordsBuffer))
      .getOrElse(Seq.empty[String])
  }

}
