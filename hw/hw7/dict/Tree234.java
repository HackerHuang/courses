/* Tree234.java */

package dict;

import java.util.TreeSet;

/**
 *  A Tree234 implements an ordered integer dictionary ADT using a 2-3-4 tree.
 *  Only int keys are stored; no object is associated with each key.  Duplicate
 *  keys are not stored in the tree.
 *
 *  @author Jonathan Shewchuk
 **/
public class Tree234 extends IntDictionary {

  /**
   *  You may add fields if you wish, but don't change anything that
   *  would prevent toString() or find() from working correctly.
   *
   *  (inherited)  size is the number of keys in the dictionary.
   *  root is the root of the 2-3-4 tree.
   **/
  Tree234Node root;

  /**
   *  Tree234() constructs an empty 2-3-4 tree.
   *
   *  You may change this constructor, but you may not change the fact that
   *  an empty Tree234 contains no nodes.
   */
  public Tree234() {
    root = null;
    size = 0;
  }

  /**
   *  toString() prints this Tree234 as a String.  Each node is printed
   *  in the form such as (for a 3-key node)
   *
   *      (child1)key1(child2)key2(child3)key3(child4)
   *
   *  where each child is a recursive call to toString, and null children
   *  are printed as a space with no parentheses.  Here's an example.
   *      ((1)7(11 16)22(23)28(37 49))50((60)84(86 95 100))
   *
   *  DO NOT CHANGE THIS METHOD.  The test code depends on it.
   *
   *  @return a String representation of the 2-3-4 tree.
   **/
  public String toString() {
    if (root == null) {
      return "";
    } else {
      /* Most of the work is done by Tree234Node.toString(). */
      return root.toString();
    }
  }

  /**
   *  printTree() prints this Tree234 as a tree, albeit sideways.
   *
   *  You're welcome to change this method if you like.  It won't be tested.
   **/
  public void printTree() {
    if (root != null) {
      /* Most of the work is done by Tree234Node.printSubtree(). */
      root.printSubtree(0);
    }
  }

  /**
   *  find() prints true if "key" is in this 2-3-4 tree; false otherwise.
   *
   *  @param key is the key sought.
   *  @return true if "key" is in the tree; false otherwise.
   **/
  public boolean find(int key) {
    Tree234Node node = root;
    while (node != null) {
      if (key < node.key1) {
        node = node.child1;
      } else if (key == node.key1) {
        return true;
      } else if ((node.keys == 1) || (key < node.key2)) {
        node = node.child2;
      } else if (key == node.key2) {
        return true;
      } else if ((node.keys == 2) || (key < node.key3)) {
        node = node.child3;
      } else if (key == node.key3) {
        return true;
      } else {
        node = node.child4;
      }
    }
    return false;
  }

  /**
   *  insert() inserts the key "key" into this 2-3-4 tree.  If "key" is
   *  already present, a duplicate copy is NOT inserted.
   *
   *  @param key is the key sought.
   **/
  /*
  // version 1: more complicated implementation
  public void insert(int key) {
    // "key" already exists, should NOT be inserted.
    if(find(key)) return;
    // "key" NOT exists
    if(root == null) {
      root = new Tree234Node(null, key);
    } else {
      Tree234Node node = root;
      while (true) {
        // restructure the 3-entries node.
        if (node.keys == 3) {
          Tree234Node n1;
          Tree234Node n2;
          if(node == root) {
            root = new Tree234Node(null, node.key2);
            n1 = new Tree234Node(root, node.key1);
            n2 = new Tree234Node(root, node.key3);
            root.child1 = n1;
            root.child2 = n2;
          } else {
            n1 = new Tree234Node(node.parent, node.key1);
            n2 = new Tree234Node(node.parent, node.key3);
            if (node == node.parent.child1) {
              node.parent.key3 = node.parent.key2;
              node.parent.key2 = node.parent.key1;
              node.parent.key1 = node.key2;
              node.parent.child4 = node.parent.child3;
              node.parent.child3 = node.parent.child2;
              node.parent.child2 = n2;
              node.parent.child1 = n1;
            } else if(node == node.parent.child2){
              node.parent.key3 = node.parent.key2;
              node.parent.key2 = node.key2;
              node.parent.child4 = node.parent.child3;
              node.parent.child3 = n2;
              node.parent.child2 = n1;
            } else {
              node.parent.key3 = node.key2;
              node.parent.child4 = n2;
              node.parent.child3 = n1;
            }
            node.parent.keys++;
            // DEBUG
            assert node.parent.keys <= 3: "ERROR: KEYS OUT OF BOUND: " + node.parent.keys;
          }
          n1.child1 = node.child1;
          n1.child2 = node.child2;
          // DON'T forget to SET parent
          if(n1.child1 != null) n1.child1.parent = n1;
          if(n1.child2 != null) n1.child2.parent = n1;
          n2.child1 = node.child3;
          n2.child2 = node.child4;
          // DON'T forget to SET parent
          if(n2.child1 != null) n2.child1.parent = n2;
          if(n2.child2 != null) n2.child2.parent = n2;
          // next iteration
          if(key < node.key2) {
            node = n1;
          } else node = n2;
          continue;
        }
        // node is NOT "leaf"
        if (node.child1 != null) {
          if (key < node.key1) {
            node = node.child1;
          } else if (node.keys == 1 || key < node.key2) {
            node = node.child2;
          } else {
            node = node.child3;
          }
        } else { // node is a "leaf"
          if (key < node.key1) {
            node.key3 = node.key2;
            node.key2 = node.key1;
            node.key1 = key;
          } else if (node.keys == 1 || key < node.key2) {
            node.key3 = node.key2;
            node.key2 = key;
          } else {
            node.key3 = key;
          }
          node.keys++;
          break;
        }
      }
    }
    size++;
  }
  */
  // version 2: simplify the insert() by defining some helper functions.
  public void insert(int key) {
    if(find(key)) return;
    if(root == null){
      root = new Tree234Node(null, key);
    } else {
      Tree234Node node = root;
      while (true) {
        if (node.keys == 3) node = restructure(node);
        if (node.child1 == null) break;
        if (key < node.key1) {
          node = node.child1;
        } else if (node.keys == 1 || key < node.key2) {
          node = node.child2;
        } else if (node.keys == 2 || key < node.key3){
          node = node.child3;
        } else node = node.child4;
      }
      if (node.keys == 2 && node.key2 < key) {
        node.key3 = key;
      } else {
        node.key3 = node.key2;
        if (node.key1 < key) node.key2 = key;
        else {
          node.key2 = node.key1;
          node.key1 = key;
        }
      }
      node.keys++;
    }
    size++;
  }

  /**
   * restructure 3-entries node by pulling out "middle" key and merging into its parent's.
   * @param node
   * @return a new root or node's parent.
   */
  private Tree234Node restructure(Tree234Node node){
    if(node == root) {
      root = new Tree234Node(null, node.key2);
      root.child1 = new Tree234Node(root, node.key1);
      root.child2 = new Tree234Node(root, node.key3);
      // set grand children of root
      setChildren(root.child1, node.child1, node.child2);
      setChildren(root.child2, node.child3, node.child4);
      return root;
    }
    int pos = moveKeyToNode(node.parent, node.key2);
    Tree234Node child1 = new Tree234Node(node.parent, node.key1);
    Tree234Node child2 = new Tree234Node(node.parent, node.key3);
    // set children of node.parent
    switch(pos){
      // left
      case 1:
        node.parent.child4 = node.parent.child3;
        node.parent.child3 = node.parent.child2;
        node.parent.child2 = child2;
        node.parent.child1 = child1;
        break;
      // middle
      case 2:
        node.parent.child4 = node.parent.child3;
        node.parent.child3 = child2;
        node.parent.child2 = child1;
        break;
      // right (case 3)
      default:
        node.parent.child4 = child2;
        node.parent.child3 = child1;
    }
    // set grand children of node.parent
    setChildren(child1, node.child1, node.child2);
    setChildren(child2, node.child3, node.child4);
    return node.parent;
  }

  /***
   * setChildren() sets the parent-children relationship between tree234nodes.
   * @param parent
   * @param child1
   * @param child2
   */
  private void setChildren(Tree234Node parent, Tree234Node child1, Tree234Node child2){
    parent.child1 = child1;
    parent.child2 = child2;
    // do NOT forget to set child1/child2.parent => parent
    if(child1 != null) child1.parent = parent;
    if(child2 != null) child2.parent = parent;
  }

  private void insertKeyInNode(Tree234Node node, int pos, int key){
    switch(pos){
      case 3:
        node.key3 = key;
        break;
      case 2:
        node.key3 = node.key2;
        node.key2 = key;
        break;
      default:
        node.key3 = node.key2;
        node.key2 = node.key1;
        node.key1 = key;
    }
  }
  /**
   * moveKeyToParent() used in the "restructure()" to insert key into parent node.
   * @param node
   * @param key
   * @return position where the "key" is inserted into "parent".
   */
  private int moveKeyToNode(Tree234Node node, int key){
    assert node.keys == 3: "ERROR: NO SPACE TO INSERT KEYS...";
    int pos;
    if(node.keys == 0) {
      insertKeyInNode(node, 1, key);
      pos = 0;
    } else if(key < node.key1){
      insertKeyInNode(node, 1, key);
      pos = 1;
    } else if(node.keys == 1 || key < node.key2) {
        insertKeyInNode(node, 2, key);
        pos = 2;
      } else {
      insertKeyInNode(node, 3, key);
      pos = 3;
    }
    node.keys++;
    return pos;
  }


  /**when traverse from root to leaf, eliminate 1-key node.
   * When remove key in the internal node, find "smallest" node in the
   * right subtree and replace the key with "smallest" value
   * (without eliminate 1-key node downwards). If underflow occurs, remedy it.
   *
   * @param key
  **/
  /*
  // version 1: more complicated version.
  public void remove(int key) {
    // empty tree.
    if (root == null) return;
    Tree234Node node = findNode(root, key);
    // key NOT found.
    if (node == null) return;
    // node is a leaf which contains > ONE keys.
    if (node.child1 == null && node.keys > 1) removeKeyFromOneNode(node, key);
    else {
      // node contains ONE key ONLY.
      if (node.keys == 1) {
        node = eliminateOneKeyNode(node);
        removeKeyFromOneNode(node, key);
      } else {
        Tree234Node tmpNode = getRightSubTree(node, key);
        while(tmpNode != null){
          if(tmpNode.child1 == null) break;
          else tmpNode = tmpNode.child1;
        }
        removeKeyFromOneNode(node, key);
        moveKeyToNode(node, tmpNode.key1);
        removeKeyFromOneNode(tmpNode, tmpNode.key1);
        remedyUnderflow(tmpNode);
      }
    }
  }
  */

  public void remove(int key){
    Tree234Node node = root;
    while(node != null){
      if(key == node.key1 || key == node.key2
              || key == node.key3) break;
      else node = moveNext(node, key);
    }
    // key NOT found.
    if(node == null) return;
    // key found.
    // CASE 1: leaf node.
    if(node.child1 == null) removeKeyFromOneNode(node, key);
    // CASE 2: internal node.
    else {
      // find the "smallest" node in the right subtree.
      Tree234Node tmpNode = getRightSubTree(node, key);
      while(tmpNode.child1 != null) tmpNode = tmpNode.child1;
      // replace node's key with tmpNode's.
      removeKeyFromOneNode(node, key);
      moveKeyToNode(node, tmpNode.key1);
      removeKeyFromOneNode(tmpNode, tmpNode.key1);
      node = tmpNode;
    }
    if(node.keys == 0){
      remedyUnderflow(node);
    }
  }

  private Tree234Node moveNext(Tree234Node node, int key){
    if(key < node.key1) return node.child1;
    else if(node.keys == 1 || key < node.key2) return node.child2;
    else if(node.keys == 2 || key < node.key3) return node.child3;
    else return node.child4;
  }

  private void remedyUnderflow(Tree234Node node){
    assert node.keys > 0:"ERROR: underflow NOT happen.";
    Tree234Node sibNode = getSiblingWithKeys(node);
    if(sibNode != null) rotation(node, sibNode);
    else fusion(node);
    if(node.parent.keys < 1) {
      if (node.parent == root) {
        node.parent = null;
        root = node;
      } else remedyUnderflow(node.parent);
    }
  }

  private Tree234Node findNode(Tree234Node node, int key){
    // traverse down the tree, eliminate one key node.
    if(node != root && node.keys == 1) node = eliminateOneKeyNode(node);
    // compare node.key* with key.
    if(key < node.key1) return findNode(node.child1, key);
    else if(key == node.key1) return node;
    else if(node.keys == 1 || key < node.key2) return findNode(node.child2, key);
    else if(key == node.key2) return node;
    else if(node.keys == 2 || key < node.key3) return findNode(node.child3, key);
    else if(key == node.key3) return node;
    else return findNode(node.child4, key);
  }

  private Tree234Node eliminateOneKeyNode(Tree234Node node){
    assert node.keys > 1: "ERROR: node contains >1 keys.";
    // node contains ONE key ONLY.
    // CASE 1: sibling contain > ONE keys => ROTATION.
    Tree234Node sibling = getSiblingWithKeys(node);
    if(sibling != null)
      return rotation(node, sibling);
    // CASE 2: sibling contains ONLY 1 key & parent contains >1 keys => FUSION.
    else if(node.parent.keys > 1)
      return fusion(node);
    // CASE 3: parent == root => delete root and fusion.
    else if(node.parent == root)
      return mergeROOT(node.parent);
    // CASE 4: INTERNAL node & children contain ONLY 1 key.
    else{
      // find the node with smallest key in the right subTree.
      Tree234Node tmp = node.child2;
      while(tmp.child1 != null){
        if(tmp.keys == 1) tmp = eliminateOneKeyNode(tmp);
        tmp = tmp.child1;
      }
      moveKeyToNode(node, tmp.key1);
      removeKeyFromOneNode(tmp, tmp.key1);
      return node;
    }
  }

  private Tree234Node mergeROOT(Tree234Node node){
    assert node.child2.keys > 1: "ERROR: INVALID OPERATION.";
    assert node.child1.keys > 1: "ERROR: INVALID OPERATION.";
    assert node != root: "ERROR: INVALID OPERATION.";
    // merge root & two 1-key children into a 3-key new root.
    mergeTwoNodes(node.child1, node.child2);
    node.child1.parent = null;
    moveKeyToNode(node.child1, node.key1);
    root = node.child1;
    size--;// tree height-1.
    return root;
  }

  private Tree234Node fusion(Tree234Node node) {
    // assert node.parent.keys < 2: "ERROR: parent has ONLY 1-key.";
    // node.parent.keys > 1
    Tree234Node sibNode = getSibling(node);
    // merge 1-key children into a 2-key node.
    mergeTwoNodes(node, sibNode);
    // merge 2-key child and 1-key parent into a 3-key node.
    int parKey = node.parent.key1;
    removeKeyFromOneNode(node.parent, parKey);
    moveKeyToNode(node, parKey);
    return node;
  }

  private void mergeTwoNodes(Tree234Node node, Tree234Node sibNode) {
    moveKeyToNode(node, sibNode.key1);
    // "isLeft" = true: node is to the left of sibNode.
    // "isLeft" = false: node is to the right of sibNode.
    boolean isLeft = getNodePos(node) < getNodePos(sibNode);
    if (isLeft) {
      switch (node.keys){
        case 1:
          node.child2 = sibNode.child1;
          node.child3 = sibNode.child2;
          break;
        default:
          node.child3 = sibNode.child1;
          node.child4 = sibNode.child2;
          break;
      }
    } else {
      switch (node.keys) {
        case 1:
          node.child3 = node.child1;
          node.child2 = sibNode.child2;
          node.child1 = sibNode.child1;
          break;
        default:
          node.child4 = node.child2;
          node.child3 = node.child1;
          node.child2 = sibNode.child2;
          node.child1 = sibNode.child1;
      }
    }
    if(sibNode.child1 != null) sibNode.child1.parent = node;
    if(sibNode.child2 != null) sibNode.child2.parent = node;
    // replace sibNode OR set sibNode to null.
    nullifyNode(sibNode);
    // node.parent.keys--; // parent's keys is taken cared by moveKeyToNode().
  }

  private void nullifyNode(Tree234Node node){
    assert node == root: "Error: node == root, invalid operation.";
    // valid operation: node != root.
    if(node.parent.child1 == node) {
      node.parent.child1 = node.parent.child2;
      node.parent.child2 = node.parent.child3;
      node.parent.child3 = node.parent.child4;
      node.parent.child4 = null;// reset child4
    } else if(node.parent.child2 == node) {
      node.parent.child2 = node.parent.child3;
      node.parent.child3 = node.parent.child4;
      node.parent.child4 = null;// reset child4
    } else if(node.parent.child3 == node) {
      node.parent.child3 = node.parent.child4;
      //node.parent.child4 = null;
    } else node.parent.child4 = null;
    node.parent = null;
    node.child1 = null;
    node.child2 = null;
  }

  private int getKeyByIndex(Tree234Node node, int index){
    switch(index){
      case 3:
        return node.key3;
      case 2:
        return node.key2;
      default:
        return node.key1;
    }
  }

  private Tree234Node getChildByIndex(Tree234Node node, int index){
    switch(index){
      case 4:
        return node.child4;
      case 3:
        return node.child3;
      case 2:
        return node.child2;
      default:
        return node.child1;
    }
  }

  private void setChildByIndex(Tree234Node node, Tree234Node child, int index){
    switch(index){
      case 4:
        node.child4 = child;
        break;
      case 3:
        node.child3 = child;
        break;
      case 2:
        node.child2 = child;
        break;
      default:
        node.child1 = child;
    }
  }

  private int getNodePos(Tree234Node node){
    if(node.parent.child4 == node) return 4;
    else if(node.parent.child3 == node) return 3;
    else if(node.parent.child2 == node) return 2;
    // cases include (1) node == root;(2) node is "empty"; (3) node == node.parent.child1.
    else return 1;
  }

  private Tree234Node rotation(Tree234Node node, Tree234Node sibling) {
    /* DEBUG */
    assert node == null: "ERROR: node is NULL.";
    assert sibling == null: "ERROR: sibling is NULL.";
    // assert node.parent.keys < 2: "parent has ONLY 1-key.";
    // key-ROTATION.
    int tmpKey;
    int stealKey;
    // determine tmpKey according to the relative position of node & sibling.
    // node is to the left of the sibling.
    int nPos = getNodePos(node);
    int sPos = getNodePos(sibling);
    if(nPos < sPos) {
      tmpKey = sibling.key1;
      stealKey = getKeyByIndex(node.parent, nPos);
      // move child1 from sibling to node.
      node.child3 = sibling.child1;
    }
    // node is to the right of the sibling.
    else {
      tmpKey = getKeyByIndex(sibling, sibling.keys);
      stealKey = getKeyByIndex(node.parent, sPos);
      node.child3 = node.child2;
      node.child2 = node.child1;
      node.child1 = getChildByIndex(sibling, sibling.keys+1);
      setChildByIndex(sibling, null, sibling.keys+1);
    }
    removeKeyFromOneNode(sibling, tmpKey);
    moveKeyToNode(node.parent, tmpKey);
    removeKeyFromOneNode(node.parent, stealKey);
    moveKeyToNode(node, stealKey);
    return node;
  }

  // getOneKeySibling() returns a sibling.
  private Tree234Node getSibling(Tree234Node node) {
    Tree234Node sibling = null;
    if (node == node.parent.child1) {
        sibling = node.parent.child2;
    } else if (node == node.parent.child2) {
        sibling = node.parent.child1;
    } else if (node == node.parent.child3) {
        sibling = node.parent.child2;
    } else sibling = node.parent.child3;
    return sibling;
  }

  // getSiblingWithKeys() returns a sibling with more than ONE key(if existed), else returns null.
  private Tree234Node getSiblingWithKeys(Tree234Node node){
    Tree234Node sibling = null;
    if(node == node.parent.child1) {
      if (node.parent.child2.keys > 1)
        sibling = node.parent.child2;
    }
    if(node == node.parent.child2) {
      if (node.parent.child1.keys > 1)
        sibling = node.parent.child1;
      else if(node.parent.keys == 2 && node.parent.child3.keys > 1)
        sibling = node.parent.child3;
    }
    if(node == node.parent.child3) {
      if(node.parent.child2.keys > 1)
        sibling = node.parent.child2;
      else if(node.parent.keys == 3 && node.parent.child4.keys > 1)
        sibling = node.parent.child4;
    }
    return sibling;
  }

  private void removeKeyFromOneNode(Tree234Node node, int key){
    assert node.keys == 1: "ERROR: 1-entry node can NOT be removed...";
    if(key == node.key1){
        node.key1 = node.key2;
        node.key2 = node.key3;
        node.key3 = 0;
    } else if(key == node.key2) {
        node.key2 = node.key3;
        node.key3 = 0;
    } else node.key3 = 0;
    node.keys--;
  }

  private Tree234Node getRightSubTree(Tree234Node node, int key){
    if(key == node.key1) return node.child2;
    else if(key == node.key2) return node.child3;
    else return node.child4;
  }

  /**
   *  testHelper() prints the String representation of this tree, then
   *  compares it with the expected String, and prints an error message if
   *  the two are not equal.
   *
   *  @param correctString is what the tree should look like.
   **/
  public void testHelper(String correctString) {
    String treeString = toString();
    System.out.println(treeString);
    if (!treeString.equals(correctString)) {
      System.out.println("ERROR:  Should be " + correctString);
    }
  }

  /**
   *  main() is a bunch of test code.  Feel free to add test code of your own;
   *  this code won't be tested or graded.
   **/
  public static void main(String[] args) {
    Tree234 t = new Tree234();

    System.out.println("\nInserting 84.");
    t.insert(84);
    t.testHelper("84");

    System.out.println("\nInserting 7.");
    t.insert(7);
    t.testHelper("7 84");

    System.out.println("\nInserting 22.");
    t.insert(22);
    t.testHelper("7 22 84");

    System.out.println("\nInserting 95.");
    t.insert(95);
    t.testHelper("(7)22(84 95)");

    System.out.println("\nInserting 50.");
    t.insert(50);
    t.testHelper("(7)22(50 84 95)");

    System.out.println("\nInserting 11.");
    t.insert(11);
    t.testHelper("(7 11)22(50 84 95)");

    System.out.println("\nInserting 37.");
    t.insert(37);
    t.testHelper("(7 11)22(37 50)84(95)");

    System.out.println("\nInserting 60.");
    t.insert(60);
    t.testHelper("(7 11)22(37 50 60)84(95)");

    System.out.println("\nInserting 1.");
    t.insert(1);
    t.testHelper("(1 7 11)22(37 50 60)84(95)");

    System.out.println("\nInserting 23.");
    t.insert(23);
    t.testHelper("(1 7 11)22(23 37)50(60)84(95)");

    System.out.println("\nInserting 16.");
    t.insert(16);
    t.testHelper("((1)7(11 16)22(23 37))50((60)84(95))");

    System.out.println("\nInserting 100.");
    t.insert(100);
    t.testHelper("((1)7(11 16)22(23 37))50((60)84(95 100))");

    System.out.println("\nInserting 28.");
    t.insert(28);
    t.testHelper("((1)7(11 16)22(23 28 37))50((60)84(95 100))");

    System.out.println("\nInserting 86.");
    t.insert(86);
    t.testHelper("((1)7(11 16)22(23 28 37))50((60)84(86 95 100))");

    System.out.println("\nInserting 49.");
    t.insert(49);
    t.testHelper("((1)7(11 16)22(23)28(37 49))50((60)84(86 95 100))");

    System.out.println("\nInserting 81.");
    t.insert(81);
    t.testHelper("((1)7(11 16)22(23)28(37 49))50((60 81)84(86 95 100))");

    System.out.println("\nInserting 51.");
    t.insert(51);
    t.testHelper("((1)7(11 16)22(23)28(37 49))50((51 60 81)84(86 95 100))");

    System.out.println("\nInserting 99.");
    t.insert(99);
    t.testHelper("((1)7(11 16)22(23)28(37 49))50((51 60 81)84(86)95(99 100))");

    System.out.println("\nInserting 75.");
    t.insert(75);
    t.testHelper("((1)7(11 16)22(23)28(37 49))50((51)60(75 81)84(86)95" +
                 "(99 100))");

    System.out.println("\nInserting 66.");
    t.insert(66);
    t.testHelper("((1)7(11 16)22(23)28(37 49))50((51)60(66 75 81))84((86)95" +
                 "(99 100))");

    System.out.println("\nInserting 4.");
    t.insert(4);
    t.testHelper("((1 4)7(11 16))22((23)28(37 49))50((51)60(66 75 81))84" +
                 "((86)95(99 100))");

    System.out.println("\nInserting 80.");
    t.insert(80);
    t.testHelper("(((1 4)7(11 16))22((23)28(37 49)))50(((51)60(66)75" +
                 "(80 81))84((86)95(99 100)))");

    System.out.println("\nFinal tree:");
    t.printTree();

/*
    System.out.println("\n=================== remove version 1 =======================");

    System.out.println("\nRemoving 80.");
    t.remove(80);
    t.testHelper("((1 4)7(11 16))22((23)28(37 49))50((51)60(66)75" +
            "(81))84((86)95(99 100))");

    System.out.println("\nRemoving 4.");
    t.remove(4);
    t.testHelper("((1)7(11 16)22(23)28(37 49))50((51)60(66)75" +
            "(81))84((86)95(99 100))");

    System.out.println("\nRemoving 66.");
    t.remove(66);
    t.testHelper("((1)7(11 16)22(23)28(37 49))50((51 60)75" +
            "(81))84((86)95(99 100))");

    System.out.println("\nRemoving 75.");
    t.remove(75);
    t.testHelper("((1)7(11 16)22(23))28((37 49)50(51)60" +
            "(81))84((86)95(99 100))");

    System.out.println("\nFinal tree:");
    t.printTree();
    */

    System.out.println("\n=================== remove version 2 =======================");

    System.out.println("\nRemoving 80.");
    t.remove(80);
    t.testHelper("(((1 4)7(11 16))22((23)28(37 49)))50(((51)60(66)75" +
            "(81))84((86)95(99 100)))");

    System.out.println("\nRemoving 4.");
    t.remove(4);
    t.testHelper("(((1)7(11 16))22((23)28(37 49)))50(((51)60(66)75" +
            "(81))84((86)95(99 100)))");

    System.out.println("\nRemoving 66.");
    t.remove(66);
    t.testHelper("(((1)7(11 16))22((23)28(37 49)))50(((51 60)75" +
            "(81))84((86)95(99 100)))");

    System.out.println("\nRemoving 75.");
    t.remove(75);
    t.testHelper("(((1)7(11 16))22((23)28(37 49)))50(((51)60" +
            "(81))84((86)95(99 100)))");

    System.out.println("\nRemoving 50.");
    t.remove(50);
    t.testHelper("((1)7(11 16))22((23)28(37 49))51((60 81)" +
            "84(86)95(99 100))");

    System.out.println("\nFinal tree:");
    t.printTree();
  }
}
