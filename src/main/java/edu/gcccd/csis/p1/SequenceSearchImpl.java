package edu.gcccd.csis.p1;

public class SequenceSearchImpl extends SequenceSearch {
    final String[] container;
    private boolean unexpectedSyntax=false;

    public SequenceSearchImpl(final String content, final String start, final String end) {
        super(content, start, end);
        container = parseContent();
    }

    private String[] parseContent() {
        if (content.length() == 0)
            return new String[] {""};
        String[] parsed = {};
        try {
            String temp = content;
            while (true) {
                //If there are no more start tags, put the rest of contents in the array
                if (!temp.contains(startTag) && temp.contains(endTag))
                        throw new IllegalArgumentException("Unexpected Syntax");
                 if (!temp.contains(startTag)) {
                     parsed = adds(parsed, temp);
                    break;
                }
                //Take what's before the start tag
                parsed = adds(parsed, temp.substring(0, temp.indexOf(startTag)));
                //Remove the start tag and everything before it
                temp = temp.substring(temp.indexOf(startTag) + startTag.length());
                //Check if there's a matching end tag
                if (!temp.contains(endTag))
                    throw new IllegalArgumentException("Unexpected Syntax");
                //Add what's between the tags
                parsed = adds(parsed, temp.substring(0, temp.indexOf(endTag)));
                //Remove the end tag
                temp = temp.substring(temp.indexOf(endTag) + endTag.length());
            }
            for(String x: parsed) {
                if (x.contains(startTag) || x.contains(endTag))
                    throw new IllegalArgumentException("Unexpected Syntax");
            }
            return parsed;
        } catch (IllegalArgumentException syntaxMistake) {
            //In this case assume start tags at the beginning
            //and end tags at the end to match all unanswered tags
            unexpectedSyntax = true;
            //Reset everything that could have partial modification from the try section
            parsed = new String[] {};
            for(int location = 0; location < content.length(); location = nextTag(location) ) {
                if(content.substring(location).startsWith(startTag) ) {
                    location = location + startTag.length();
                    parsed = adds(parsed, "+" + content.substring(location, nextTag(location)));
                }else if(content.substring(location).startsWith(endTag) ) {
                    location = location + endTag.length();
                    parsed = adds(parsed, "-" + content.substring(location, nextTag(location)));
                } else {//This should only occur at the start, and only if it does not begin with a tag
                    parsed = adds(parsed, "." + content.substring(location, nextTag(location)));
                }
                //Always move to the next tag
                location = nextTag(location);
            }
            return parsed;
        }
    }

    private int nextTag(int position) {
        //Create a substring starting where you want to search
        String temp = content.substring(position);
        //If it contains only a start tag, return its position
        if(temp.contains(startTag) && !temp.contains(endTag) )
            return temp.indexOf(startTag)+position;
        //If it contains only an end tag, return its position
        if(!temp.contains(startTag) && temp.contains(endTag) )
                return temp.indexOf(endTag)+position;
        //If it contains both tags, return the position of the tag that occurs first
        if(temp.contains(startTag) && temp.contains(endTag) ) {
            if (temp.indexOf(startTag) < temp.indexOf(endTag)) {
                return temp.indexOf(startTag)+position;
            } else return temp.indexOf(endTag)+position;
        }
        //If there are no more tags, return the length of the string
        return (temp.length()+position);
    }

    @Override
    public String[] getAllTaggedSequences() {
        if (!unexpectedSyntax) {
            String[] inTags = new String[container.length / 2];
            for (int x = 1; x < container.length; x = x + 2)
                inTags[(x / 2)] = container[x];
            return inTags;
        } else {
            String[] allTagged = {};
            for(int x = 0; x < container.length -2; x++) {
                if (container[x].startsWith("+") && container[x+1].startsWith("-") )
                    allTagged = adds(allTagged, container[x].substring(1));
            }
            return allTagged;
        }
    }

    @Override
    public String getLongestTaggedSequence() {
        String longest = null;
        for(String x : getAllTaggedSequences()) {
            if (longest == null || longest.length() <= x.length())
                longest = x;
        }
        return longest;
    }

    @Override
    public String displayStringArray() {
        String display = "";
        if (!unexpectedSyntax) {
            for (String x : getAllTaggedSequences())
                display = display.concat(x + " : " + x.length() + "\n");
        } else {
            for (String x : getAllTaggedSequences())
                display = display.concat(x.substring(1) + " : " + (x.length()-1) + "\n");
        }
       return display;
    }

    @Override
    public String toString() {
        String temp = "";
        for (String x: container) {
            if (!unexpectedSyntax)
            temp = temp.concat(x);
            if (unexpectedSyntax)
                temp = temp.concat(x.substring(1));
        }
        return temp;
    }
}