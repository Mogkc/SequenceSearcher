package edu.gcccd.csis.p1;

/**
 * A SequenceSearch object, once constructed contains a content String as well as a start-tag and an end-tag.
 * The constructor ensures that the content is not null and while the start and end tag are allowed to be the same,
 * they cannot contain each other.
 * <p>
 * The class' abstract methods are suppose to
 * - find all tagged sequences in the content
 * - identify the longest tagged sequence.
 * <p>
 * Example:
 * <p>
 * startTag= "[["
 * endTag = "]]"
 *
 * content= "[[Lorem ipsum]] dolor sit amet, [[consectetur adipiscing elit]], sed do eiusmod tempor incididunt ut
 * labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco
 * laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in
 * voluptate velit esse cillum dolore eu fugiat nulla pariatur."
 *
 * The content is considered syntactically correct and simple.
 * I.e there are no nested tags and every start-tag is followed by an end-tag.
 *
 * <p>
 * getAllTaggedSequences() would return {"Lorem ipsum","consectetur adipiscing elit"}
 * getLongestTaggedSequence() would return "consectetur adipiscing elit"
 */
public abstract class SequenceSearch implements Presentable {

    final String startTag;
    final String endTag;
    final String content;

    /**
     * The one and only constructor
     * @param content {@link String} content with the some tags inside
     * @param startTag {@link String}
     * @param endTag {@link String}
     */
    public SequenceSearch(final String content, final String startTag, final String endTag) {
        if (content == null || startTag == null || endTag == null) {
            throw new IllegalArgumentException("Values for neither Content nor Start nor End can be null.");
        }
        if (!startTag.equals(endTag)) { // which is ok ...
            if (startTag.contains(endTag) || endTag.contains(startTag)) { // .. but it's not OK that the startTag contains the endtag or vice versa.
                throw new IllegalArgumentException("Start and end tag are allowed to be the same, but otherwise must not contain each other.");
            }
        }
        this.content = content;
        this.startTag = startTag;
        this.endTag = endTag;
    }

    /**
     * Static helper method, "adds" the given Strings s a String Array, by creating a new longer StringArray.
     *
     * @param a {@link String[]} source string array
     * @param s {@link String} that needs to be added
     * @return {@link String[]} newly created string array, containing all the elements from the provided array, plus the new string.
     */
    static String[] adds(final String[] a, final String s) {
        final String[] sa;
        if (a!=null && 0<a.length) {
            sa = new String[a.length + 1];
            System.arraycopy(a, 0, sa, 0, a.length);
            sa[a.length] = s;
        } else {
            sa = new String[]{s};
        }
        return sa;
    }

    /**
     * @return {@link String[]} of all sub-string, within the content, which are enclosed, in startTag and endTag.
     * If no tagged sub-strings are found, an empty String[] is returned.
     */
    public abstract String[] getAllTaggedSequences();

    /**
     * @return {@link String} the longest sub-string within the content, which is enclosed, in startTag and endTag.
     * If unlcear, the substring that appears last in the content will be returned.
     * If no tagged sequence was found, null is returned.
     */
    public abstract String getLongestTaggedSequence();

}
