package br.com.mcoder.blog.config;

import br.com.mcoder.blog.models.Account;
import br.com.mcoder.blog.models.Authority;
import br.com.mcoder.blog.models.Post;
import br.com.mcoder.blog.repositories.AuthorityRepository;
import br.com.mcoder.blog.services.AccountService;
import br.com.mcoder.blog.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    private PostService  postService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public void run(String... args) throws Exception {

        Authority user = new Authority();
        user.setName("ROLE_USER");
        authorityRepository.save(user);

        Authority admin = new Authority();
        admin.setName("ROLE_ADMIN");
        authorityRepository.save(admin);

        Account account1 = new Account();
        Account account2 = new Account();

        account1.setFirstName("user");
        account1.setLastName("user");
        account1.setEmail("user.user@email.com");
        account1.setPassword("password");
        Set<Authority> authorities1 = new HashSet<>();
        authorityRepository.findById("ROLE_USER").ifPresent(authorities1::add);
        account1.setAuthorities(authorities1);

        account2.setFirstName("admin");
        account2.setLastName("admin");
        account2.setEmail("admin.admin@email.com");
        account2.setPassword("password");
        Set<Authority> authorities2 = new HashSet<>();
        authorityRepository.findById("ROLE_USER").ifPresent(authorities2::add);
        authorityRepository.findById("ROLE_ADMIN").ifPresent(authorities2::add);
        account2.setAuthorities(authorities2);

        accountService.save(account1);
        accountService.save(account2);

        List<Post> posts = postService.getAll();

        if (posts.size() == 0){
            Post post1 = new Post();
            post1.setTitle("title of post 1");
            post1.setBody("this is the body of post 1");
            post1.setAccount(account1);

            Post post2 = new Post();
            post2.setTitle("title of post 2");
            post2.setBody("this is the body of post 2");
            post2.setAccount(account2);

            postService.save(post1);
            postService.save(post2);
        }
    }
}
