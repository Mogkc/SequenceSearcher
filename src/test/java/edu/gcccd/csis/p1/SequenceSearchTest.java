package edu.gcccd.csis.p1;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SequenceSearchTest {

    private String s0 = "%sJava%s is a %sprogramming language%s created by %sJames Gosling%s from %sSun Microsystems%s " +
            "(Sun) in 1991. The target of Java is to write a program once and then run this program on multiple " +
            "operating systems. The first publicly available version of Java (Java 1.0) was released in 1995. " +
            "Sun Microsystems was acquired by the Oracle Corporation in 2010. " +
            "Oracle has now the steermanship for Java. In 2006 Sun started to make Java available under the " +
            "GNU General Public License (GPL). Oracle continues this project called %sOpenJDK.%s";

    private String s1 = "Java is a %sprogramming language%s created by %sJames Gosling%s from %sSun Microsystems%s " +
            "(Sun) in 1991. The target of Java is to write a program once and then run this program on multiple " +
            "operating systems. The first publicly available version of Java (Java 1.0) was released in 1995. " +
            "Sun Microsystems was acquired by the Oracle Corporation in 2010. " +
            "Oracle has now the steermanship for Java. In 2006 Sun started to make Java available under the " +
            "GNU General Public License (GPL). Oracle continues this project called %sOpenJDK.%s";

    private String s2 = "Java is a %sprogramming language%s created by %sJames Gosling%s from %sSun Microsystems%s " +
            "(Sun) in 1991. The target of Java is to write a program once and then run this program on multiple " +
            "operating systems. The first publicly available version of Java (Java 1.0) was released in 1995. " +
            "Sun Microsystems was acquired by the Oracle Corporation in 2010. " +
            "Oracle has now the steermanship for Java. In 2006 Sun started to make Java available under the " +
            "GNU General Public License (GPL). Oracle continues this project called OpenJDK.";

    private String s3 = "%sJava%s is a programming language created by James Gosling from Sun Microsystems " +
            "(Sun) in 1991. The target of Java is to write a program once and then run this program on multiple " +
            "operating systems. The first publicly available version of Java (Java 1.0) was released in 1995. " +
            "Sun Microsystems was acquired by the Oracle Corporation in %s2010%s. " +
            "Oracle has now the steermanship for Java. In 2006 Sun started to make Java available under the " +
            "GNU General Public License (GPL). Oracle continues this project called OpenJDK.";

    private String s4 = "Java is a programming language created by James Gosling from Sun Microsystems " +
            "(Sun) in 1991. The target of Java is to write a program once and then run this program on multiple " +
            "operating systems. The first publicly available version of Java (Java 1.0) was released in 1995. " +
            "Sun Microsystems was acquired by the Oracle Corporation in 2010. " +
            "Oracle has now the steermanship for Java. In 2006 Sun started to make Java available under the " +
            "GNU General Public License (GPL). Oracle continues this project called OpenJDK.";

    // The here tested implementation is in the abstract class and this test is shown here, only for clarification
    @Test
    public void constructionTest() {
        String s = "abc def ghi";
        // tags can have different length;
        SequenceSearch s1 = new SequenceSearchImpl(s, "a", "bc");
        // tags can be the same
        SequenceSearch s2 = new SequenceSearchImpl(s, "a", "bc");
        // tags cannot contain tags
        try {
            SequenceSearch s3 = new SequenceSearchImpl(s, "a", "ab");
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
        try {
            SequenceSearch s4 = new SequenceSearchImpl(s, "ac", "c");
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    // Finding all text sequences that are enclosed in beginning and end tags.
    @Test
    public void getAllTaggedSequences() throws Exception {

        final String d0 = "<";
        final String d1 = "/>";

        String s = String.format(s0, d0, d1, d0, d1, d0, d1, d0, d1, d0, d1);
        String[] sa = new SequenceSearchImpl(s, d0, d1).getAllTaggedSequences();
        assertEquals("Java", sa[0]);
        assertEquals("programming language", sa[1]);
        assertEquals("James Gosling", sa[2]);
        assertEquals("Sun Microsystems", sa[3]);
        assertEquals("OpenJDK.", sa[4]);

        s = String.format(s1, d0, d1, d0, d1, d0, d1, d0, d1);
        sa = new SequenceSearchImpl(s, d0, d1).getAllTaggedSequences();
        assertEquals("programming language", sa[0]);
        assertEquals("James Gosling", sa[1]);
        assertEquals("Sun Microsystems", sa[2]);
        assertEquals("OpenJDK.", sa[3]);

        s = String.format(s2, d0, d1, d0, d1, d0, d1);
        sa = new SequenceSearchImpl(s, d0, d1).getAllTaggedSequences();
        assertEquals("programming language", sa[0]);
        assertEquals("James Gosling", sa[1]);
        assertEquals("Sun Microsystems", sa[2]);
    }

    // Finding the longest of all tagged sequences.
    @Test
    public void getLongestTaggedSequence() throws Exception {
        String d0 = "{{";
        String d1 = "}}";

        String s = String.format(s2, d0, d1, d0, d1, d0, d1);
        String t = new SequenceSearchImpl(s, d0, d1).getLongestTaggedSequence();

        assertEquals("programming language", t);
        // find the later one, if same length
        s = String.format(s3, d0, d1, d0, d1);
        t = new SequenceSearchImpl(s, d0, d1).getLongestTaggedSequence();

        assertEquals("2010", t);

        // no tags in content whatsoever.
        s = String.format(s4, d0, d1);
        assertEquals(0, new SequenceSearchImpl(s, d0, d1).getAllTaggedSequences().length);

        t = new SequenceSearchImpl(s, d0, d1).getLongestTaggedSequence();
        assertNull(t);
    }

    // Making sure that all sequences are found, even if start and end tags look the same.
    @Test
    public void getAllTaggedSequencesEqualDelimiters() throws Exception {

        String d = "#";

        String s = String.format(s0, d, d, d, d, d, d, d, d, d, d);
        String[] sa = new SequenceSearchImpl(s, d, d).getAllTaggedSequences();
        assertEquals("Java", sa[0]);
        assertEquals("programming language", sa[1]);
        assertEquals("James Gosling", sa[2]);
        assertEquals("Sun Microsystems", sa[3]);
        assertEquals("OpenJDK.", sa[4]);

        d = "##";
        s = String.format(s1, d, d, d, d, d, d, d, d);
        sa = new SequenceSearchImpl(s, d, d).getAllTaggedSequences();
        assertEquals("programming language", sa[0]);
        assertEquals("James Gosling", sa[1]);
        assertEquals("Sun Microsystems", sa[2]);
        assertEquals("OpenJDK.", sa[3]);

        d = "###";
        s = String.format(s2, d, d, d, d, d, d);
        sa = new SequenceSearchImpl(s, d, d).getAllTaggedSequences();
        assertEquals("programming language", sa[0]);
        assertEquals("James Gosling", sa[1]);
        assertEquals("Sun Microsystems", sa[2]);
    }

    // Interface Implementation
    @Test
    public void testPresentable() {
        String d = "###";
        String s = String.format(s2, d, d, d, d, d, d);
        String p = new SequenceSearchImpl(s, d, d).displayStringArray();
        assertTrue(p.startsWith("programming language : 20\n"));
        assertTrue(p.endsWith("Sun Microsystems : 16\n"));
    }

    // ToString prints the content, with all tags removed.
    @Test
    public void testToString() {
        String d0 = "<";
        String d1 = "/>";
        String s = String.format(s2, d0, d1, d0, d1, d0, d1);
        String p = new SequenceSearchImpl(s, d0, d1).toString();
        assertFalse(p.contains(d0));
        assertFalse(p.contains(d1));
        assertTrue(p.startsWith("Java is a programming language created by James Gosling"));
    }
}