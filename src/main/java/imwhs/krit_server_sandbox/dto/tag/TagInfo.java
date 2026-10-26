package imwhs.krit_server_sandbox.dto.tag;

import imwhs.krit_server_sandbox.domain.Tag;

public record TagInfo(String name) {

    public static TagInfo from(Tag tag) {
        return new TagInfo(tag.getName());
    }

}
