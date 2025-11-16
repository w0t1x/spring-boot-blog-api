package org.example.dto;

public class TagUsageDTO {
    private final String TagName;
    private final long postCount;

    public TagUsageDTO(String TagName, long postCount) {
        this.TagName = TagName;
        this.postCount = postCount;
    }

    public String getTagName() {
        return TagName;
    }

    public long getPostCount() {
        return postCount;
    }
}
