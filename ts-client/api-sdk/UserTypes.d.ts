export const tsDontComplain: never;

export interface CreateUserModel {
	name: string 
}
export interface User {
	createdAt: Date ,
	id: string ,
	name: string 
}
export interface ErrorFormat {
	detail: string ,
	internalCode: number ,
	title: string 
}