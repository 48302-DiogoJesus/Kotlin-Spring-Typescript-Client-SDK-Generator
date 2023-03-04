import UserTypes from "./UserTypes";
import { ServerResponse } from "./ServerResponse";
import { replacePathAndQueryVariables } from "./Utils";

export default function BuildSDK(apiBaseUrl: string) { return {
	USERS: {
		delete: (args: { 
	params: {	userId: UUID},
 }): Promise<ServerResponse<void, UserTypes.ErrorFormat>> => 
	fetch(
		replacePathAndQueryVariables(`${apiBaseUrl}/api/users/{id}`, args.params, undefined),
		{
			method: "DELETE",
		}
	)
		.then(res => res.json())
,
		get: (args: { 
	params: {	id: UUID},
 }): Promise<ServerResponse<UserTypes.User, UserTypes.ErrorFormat>> => 
	fetch(
		replacePathAndQueryVariables(`${apiBaseUrl}/api/users/api/users/{uid}`, args.params, undefined),
		{
			method: "GET",
		}
	)
		.then(res => res.json())
,
		create: (args: { 
	body: UserTypes.CreateUserModel,
 }): Promise<ServerResponse<UserTypes.User, UserTypes.ErrorFormat>> => 
	fetch(
		replacePathAndQueryVariables(`${apiBaseUrl}/api/users/api/users/`, undefined, undefined),
		{
			method: "POST",
			headers: {"Content-Type": "application/json"},
			body: JSON.stringify(args.body)
		}
	)
		.then(res => res.json())
,
	},
}}
