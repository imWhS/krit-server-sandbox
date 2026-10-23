package imwhs.krit_server_sandbox.service;

import imwhs.krit_server_sandbox.domain.Tag;
import imwhs.krit_server_sandbox.dto.tag.TagInfo;
import imwhs.krit_server_sandbox.exception.tag.TagDuplicateException;
import imwhs.krit_server_sandbox.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagInfo register(String name) {
        if (tagRepository.existsByName(name)) {
            throw new TagDuplicateException(name);
        }

        Tag tag = Tag.create(name);
        tagRepository.save(tag);
        return TagInfo.from(tag);
    }

    public void delete(String name) {
        tagRepository.deleteByName(name);
    }

}
