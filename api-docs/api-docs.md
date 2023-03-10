## Global Error Format

```json
ErrorFormat {
	detail: string ,
	internalCode: number ,
	title: string 
}
```
---

### Users

#### GET /api/users/{id: string}/{a: string}/{b: string}

`Response Body`

```json
User {
	createdAt: Date ,
	id: string ,
	name: string 
}
```
#### DELETE /api/users/{id: string}

`Response Body`

```json
void
```
#### POST /api/users/

`Request Body`

```json
CreateUserModel {
	name: string 
}
```
`Response Body`

```json
User {
	createdAt: Date ,
	id: string ,
	name: string 
}
```


---

### Posts

#### GET /api/posts/{id: string}

`Response Body`

```json
Post {
	author: User ,
	content?: string | null,
	createdAt: Date ,
	id: string ,
	title: string 
}
```
#### POST /api/posts/

`Request Body`

```json
CreatePostModel {
	authorId: string ,
	content?: string | null,
	title: string 
}
```
`Response Body`

```json
Post {
	author: User ,
	content?: string | null,
	createdAt: Date ,
	id: string ,
	title: string 
}
```


