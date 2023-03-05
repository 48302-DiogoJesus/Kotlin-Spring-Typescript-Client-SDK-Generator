import BuildSDK from "./api-sdk/sdk";
import { Post } from "./api-sdk/UserTypes";

const {
  posts: postsEndpoints,
  users: usersEndpoints,
} = BuildSDK("http://localhost:8080");

(async () => {
  try {
    // CREATE USER (will be POST author)
    // (On the server the return type of the handler is ResponseEntity<HandlerResponse<Post, ErrorFormat>>)
    const {
      error: createUserError,
      data: newUser,
    } = await usersEndpoints.create({ name: "Maria" });

    if (createUserError) {
      throw createUserError;
    }

    const newUserId: string = newUser.id;

    // CREATE POST
    // (On the server the return type of the handler is ResponseEntity<HandlerResponse<Post, ErrorFormat>>)
    const {
      error: createPostError,
      data: newPost,
    } = await postsEndpoints.create({
      authorId: newUserId,
      title: "Some post",
      content: "The post content",
    });

    if (createPostError) {
      throw createPostError;
    }

    console.log("Post created:", newPost);

    // GET POST
    // (On the server the return type of the handler is ResponseEntity<Post>)
    const post: Post = await postsEndpoints.get({ id: newPost.id });

    console.log("Got post:", post);
  } catch (err) {
    console.log("Error", err);
  }
})();
