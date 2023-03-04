export const tsDontComplain: never;

export interface ErrorFormat {
	detail: string ,
	internalCode: number ,
	title: string 
}
export interface Instant {
	seconds: number ,
	nanos: number 
}
export interface UUID {
	mostSigBits: number ,
	leastSigBits: number 
}
export interface User {
	createdAt: Instant ,
	id: UUID ,
	name: string 
}
export interface CreateUserModel {
	name: string 
}