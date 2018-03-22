package edu.gcccd.csis.p1;

public class SequenceSearchImpl extends SequenceSearch {
    final String[] container;

    public SequenceSearchImpl(final String content, final String start, final String end) {
        super(content, start, end);
        container = parseContent();
    }

    private String[] parseContent() {
        String[] parsed = new String[0];

        String temp = content;
        while(true) {
            //If there are no more start tags, put the rest of contents in the array
            if (!temp.contains(startTag)){
                parsed = adds(parsed, temp);
                break;
            }
            //Take what's before the start tag
            parsed = adds(parsed, temp.substring(0, temp.indexOf(startTag)));
            //Remove the start tag and everything before it
            temp = temp.substring(temp.indexOf(startTag) + startTag.length());
            int fin = temp.indexOf(endTag);
            //Add what's between the tags
            parsed = adds(parsed, temp.substring(0, fin));
            //Remove the end tag
            temp = temp.substring(fin + endTag.length());
        }
        return parsed;
    }

    @Override
    public String[] getAllTaggedSequences() {
        String[] inTags = new String[container.length/2];
        for(int x = 1; x < container.length; x=x+2)
            inTags[(x/2)] = container[x];
        return inTags;
    }

    @Override
    public String getLongestTaggedSequence() {
        String longest = null;
        int size = 0;
        for (int x = 1; x < container.length; x=x+2) {
            if (size <= container[x].length() ) {
                longest = container[x];
                size = longest.length();
            }
        }
        return longest;
    }

    @Override
    public String displayStringArray() {
        String display = "";
       for(int x = 1; x < container.length; x=x+2) {
           display = display.concat(container[x] + " : " + container[x].length() + "\n");
       }
       return display;
    }

    @Override
    public String toString() {
        String temp = "";
        for (String x: container) {
            temp = temp.concat(x);
        }
        return temp;
    }
}