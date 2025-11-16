package org.example.dto.mapper;

import org.example.dto.AuthorDto;
import org.example.dto.PostResponseDTO;
import org.example.dto.TagDto;
import org.example.model.Post;
import org.example.model.Tag;
import org.example.model.User;

import java.util.List;

public class PostMapper {
    public static PostResponseDTO toDto(Post post){
        AuthorDto authorDto = toAuthorDto(post.getAuthor());

        List<TagDto> tagDto = post.getTags().stream()
                .map(PostMapper::toTagDto)
                .toList();

        int commentsCount = post.getComments() == null ? 0 : post.getComments().size();

        return new PostResponseDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                authorDto,
                tagDto,
                commentsCount
        );
    }

    private static AuthorDto toAuthorDto(User user) {
        if (user == null) return null;
        return new AuthorDto(user.getId(), user.getName(), user.getEmail());
    }

    private static TagDto toTagDto(Tag tag) {
        return new TagDto(tag.getId(), tag.getName());
    }
}
