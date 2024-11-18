package io.github.intisy.gui.javafx;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class ExpandableString {
    String value;
    boolean expanded;
    List<ExpandableString> subStrings;
    ExpandableString parent;

    public ExpandableString(String value) {
        this.value = value;
        this.subStrings = new ArrayList<>();
    }

    public ExpandableString getRawValue() {
        return new ExpandableString(value);
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setParent(ExpandableString parent) {
        this.parent = parent;
    }

    public ExpandableString getParent() {
        return parent;
    }

    public List<ExpandableString> getSubStrings() {
        return subStrings;
    }

    public String getValue() {
        return value;
    }

    public boolean hasChildren() {
        return !subStrings.isEmpty();
    }

    public void addSubString(ExpandableString subExpandableString) {
        this.subStrings.add(subExpandableString);
        subExpandableString.setParent(this);
    }

    public ExpandableString withSubString(ExpandableString subExpandableString) {
        addSubString(subExpandableString);
        return this;
    }

    public void display(String prefix) {
        System.out.println(prefix + value);
        for (ExpandableString sub : subStrings) {
            sub.display(prefix + "  ");
        }
    }
}
