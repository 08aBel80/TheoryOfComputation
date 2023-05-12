package tests;

import javafx.util.Pair;
import obects.NFA;
import obects.State;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.TestDataFileReader;

import java.util.ArrayList;

public class NFATest {
    private State[] states;
    private NFA nfa1;
    private NFA nfa2;

    @BeforeMethod
    public void setUp() {
        states = new State[20];
        for (int i = 0; i < 20; i++) {
            states[i] = new State();
        }
        // (0) -a->((1))
        states[0].addState(states[1], 'a');
        nfa1 = new NFA(states[0], states[1]);

        // (2) -b->((3))
        states[2].addState(states[3], 'b');
        nfa2 = new NFA(states[2], states[3]);

    }

    @Test
    public void test1() {
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
    public void testConcat() {
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
    public void testStar() {
        nfa1.star();
        Assert.assertEquals(nfa1.toString(),
                "2 2 2\n" +
                        "0 1\n" +
                        "1 a 1\n" +
                        "1 a 1");
    }

    @DataProvider
    public Object[][] regexProvider() {
        ArrayList<Pair<String,String>> inOutPut = TestDataFileReader.readInputOutputPairs();
        Object[][] result = new Object[inOutPut.size()][2];
        for(int i = 0; i < inOutPut.size(); i++) {
            result[i][0] = inOutPut.get(i).getKey();
            result[i][1] = inOutPut.get(i).getValue();
        }
        return result;
    }
    @Test(dataProvider = "regexProvider", enabled = false)
    public void testRegex(String input,String output) {
        NFA nfa = new NFA(input);

        Assert.assertEquals(nfa.toString(), output);
    }
}
