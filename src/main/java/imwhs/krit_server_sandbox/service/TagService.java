package imwhs.krit_server_sandbox.service;

import imwhs.krit_server_sandbox.domain.Tag;
import imwhs.krit_server_sandbox.dto.tag.TagInfo;
import imwhs.krit_server_sandbox.exception.tag.TagDuplicateException;
import imwhs.krit_server_sandbox.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TagService {

    private final TagRepository tagRepository;

    @Transactional
    public TagInfo register(String name) {
        String strippedName = name.strip();
        if (tagRepository.existsByName(strippedName)) {
            throw new TagDuplicateException(strippedName);
        }

        Tag tag = Tag.create(strippedName);
        tagRepository.save(tag);
        return TagInfo.from(tag);
    }

    @Transactional
    public void delete(String name) {
        String strippedName = name.strip();
        tagRepository.deleteByName(strippedName);
    }

}
