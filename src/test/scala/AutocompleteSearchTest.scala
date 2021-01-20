import org.scalatest.BeforeAndAfter
import org.scalatest.flatspec._
import org.scalatest.matchers.should

import scala.io.Source

class AutocompleteSearchTest extends AnyFlatSpec with should.Matchers with BeforeAndAfter {

  var words: Seq[String] = Seq.empty[String]

  before {
    words = Source.fromResource("words.txt").getLines().toSeq
  }

  it should "add word" in {
    val word: TreeNode = new TreeNode
    AutocompleteSearch.addWord(word, "abc", 0)
    word.children('a').isDefined shouldBe true
    word.children('a').get.children('b').isDefined shouldBe true
    word.children('a').get.children('b').get.children('c').isDefined shouldBe true
    word.children('a').get.children('b').get.children('c').get.isWord shouldBe true
  }

  it should "handle case sensitive and special characters when adding word" in {
    val word: TreeNode = new TreeNode
    AutocompleteSearch.addWord(word, "AB$", 0)
    word.children('A').isEmpty shouldBe true
    word.children('a').isDefined shouldBe true
    word.children('a').get.children('b').isDefined shouldBe true
    word.children('a').get.children('b').get.children('$').isDefined shouldBe true
    word.children('a').get.children('b').get.children('$').get.isWord shouldBe true
  }

  it should "return an empty tree when the word is empty" in {
    val word: TreeNode = new TreeNode
    AutocompleteSearch.addWord(new TreeNode, "", 0)
    word.isWord shouldBe false
    word.children.flatten.length should equal (0)
  }

  it should "return a tree with all the words" in {
    val result: TreeNode = AutocompleteSearch.addWords(new TreeNode, Seq("ab", "b"))

    result.children('a').isDefined shouldBe true
    result.children('a').get.isWord shouldBe false
    result.children('a').get.children('b').isDefined shouldBe true
    result.children('a').get.children('b').get.isWord shouldBe true
    result.children('a').get.isWord shouldBe false
    result.children('b').isDefined shouldBe true
    result.children('b').get.isWord shouldBe true
  }

  it should "return a empty tree when there is no word" in {
    val result: TreeNode = AutocompleteSearch.addWords(new TreeNode, Seq.empty[String])

    result.isWord shouldBe false
    result.children.flatten.length should equal (0)
  }

  it should "return the node of the last char of keyword" in {
    val wordsTree: TreeNode = AutocompleteSearch.addWords(new TreeNode, Seq("project", "professor"))
    val result = AutocompleteSearch.findStartingNode(Some(wordsTree), "pro")

    result.isDefined shouldBe true
    result.get.children('j').isDefined shouldBe true
    result.get.children('f').isDefined shouldBe true
  }

  it should "return None when the keyword doesn't match" in {
    val wordsTree: TreeNode = AutocompleteSearch.addWords(new TreeNode, Seq("project", "professor"))
    val result = AutocompleteSearch.findStartingNode(Some(wordsTree), "happy")

    result.isEmpty shouldBe true
  }

  it should "return an empty treeNode when there is no keyword" in {
    val wordsTree: TreeNode = AutocompleteSearch.addWords(new TreeNode, Seq("project", "professor"))
    val result = AutocompleteSearch.findStartingNode(Some(wordsTree), "")

    result.isEmpty shouldBe true
  }

  it should "return 4 suggested words in order" in {
    val wordsSeq: Seq[String] = Seq("proactive", "progenex", "progeria", "progesterone")
    val result = AutocompleteSearch.search(words, "pro")

    result should contain inOrder (wordsSeq.head, wordsSeq(1), wordsSeq(2), wordsSeq(3))
    result.length should equal (4)
  }

  it should "return only 2 suggested words when there is no other matching word" in {
    val result = AutocompleteSearch.search(words, "project")
    result.length should be (2)
  }

  it should "return an empty sequence when there is no matching word" in {
    val result = AutocompleteSearch.search(words, "happy")
    result.length should be (0)
  }
}
