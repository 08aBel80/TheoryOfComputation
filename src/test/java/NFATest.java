import obects.NFA;
import obects.Node;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;

public class NFATest {
   private Node[] nodes;
   private NFA nfa1;
   private NFA nfa2;
   @BeforeTest
   public void setUp() {
      nodes = new Node[20];
      for(int i = 0; i < 20; i++){
         nodes[i] = new Node();
      }
      // (0) -a->((1))
      nodes[0].addNode(nodes[1], 'a');
      ArrayList<Node> acceptNodes1 = new ArrayList<Node>();
      acceptNodes1.add(nodes[1]);
      nfa1 = new NFA(nodes[0], acceptNodes1);

      // (2) -b->((3))
      nodes[2].addNode(nodes[3], 'b');
      ArrayList<Node> acceptNodes2 = new ArrayList<Node>();
      acceptNodes2.add(nodes[3]);
      nfa2 = new NFA(nodes[2], acceptNodes2);

   }
   @Test
    public void test1(){
       Assert.assertEquals(nfa1.toString(),
               "2 1 1\n" +
               "1\n" +
               "1 a 1\n" +
               "0");
      Assert.assertEquals(nfa2.toString(),
              "2 1 1\n" +
              "1\n" +
              "1 b 1\n" +
              "0");
   }

   @Test
   public void testConcat(){
         nfa1.concat(nfa2);
         Assert.assertEquals(nfa1.toString(),
                 "3 1 2\n" +
                 "2\n" +
                 "1 a 1\n" +
                 "1 b 2\n" +
                 "0");
   }
   @Test
   public void testUnion() {
      nfa1.union(nfa2);
      Assert.assertEquals(nfa1.toString(),
              "3 2 2\n" +
              "1 2\n" +
              "2 a 1 b 2\n" +
              "0\n" +
              "0");
   }
   @Test
   public void testStar(){
      nfa1.star();
      Assert.assertEquals(nfa1.toString(),
              "2 1 2\n" +
                      "1\n" +
                      "1 a 1\n" +
                      "1 a 1");
   }
}
