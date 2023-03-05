export const tsDontComplain: never;

export interface CreatePostModel {
	authorId: string ,
	content?: string | null,
	title: string 
}
export interface User {
	createdAt: Date ,
	id: string ,
	name: string 
}
export interface Post {
	author: User ,
	content?: string | null,
	createdAt: Date ,
	id: string ,
	title: string 
}
export interface ErrorFormat {
	detail: string ,
	internalCode: number ,
	title: string 
}
export interface CreateUserModel {
	name: string 
}