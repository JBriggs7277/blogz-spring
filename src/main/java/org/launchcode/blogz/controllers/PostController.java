package org.launchcode.blogz.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.launchcode.blogz.models.Post;
import org.launchcode.blogz.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PostController extends AbstractController {

	@RequestMapping(value = "/blog/newpost", method = RequestMethod.GET)
	public String newPostForm() {
		return "newpost";
	}
	
	@RequestMapping(value = "/blog/newpost", method = RequestMethod.POST)
	public String newPost(HttpServletRequest request, Model model) {
		
		// TODO - implement newPost
		
		//get request parameters
		String title = request.getParameter("title");
		String body = request.getParameter("body");
		HttpSession thisSession = request.getSession();
		User author = getUserFromSession(thisSession); 
				
		//validate parameters, resend form if not
		if(title == null || title == "" || body == null || body == "")
		{
			model.addAttribute("error", "We need both a title and a body!");
			model.addAttribute("value", title);
			model.addAttribute("body", body);
			return "newpost";
		}
		
		//if valid, create new Post
		Post post = new Post(title, body, author);
		postDao.save(post);
		int post_id = post.getUid();
		String post_user = author.getUsername();
		
		
		return "redirect:/blog/" + post_user + "/" + post_id; // TODO - this redirect should go to the new post's page  		
	}
	
	@RequestMapping(value = "/blog/{username}/{uid}", method = RequestMethod.GET)
	public String singlePost(@PathVariable String username, @PathVariable int uid, Model model) {
		
		// TODO - implement singlePost
		
		//get the given post
		Post post = postDao.findByUid(uid);
		
		//pass the post into the template
		model.addAttribute("post", post);
		
		return "post";
	}
	
	@RequestMapping(value = "/blog/{username}", method = RequestMethod.GET)
	public String userPosts(@PathVariable String username, Model model) {
		
		// TODO - implement userPosts
		
		User user = userDao.findByUsername(username);
		
		//get all of the user's posts
		List<Post> posts = user.getPosts();
		
		//pass them into the template
		model.addAttribute("posts", posts);
		
		return "blog";
	}
	
}
