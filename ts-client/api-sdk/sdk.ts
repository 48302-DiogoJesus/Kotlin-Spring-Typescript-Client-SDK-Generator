import UserTypes from "./UserTypes";
import { ServerResponse } from "./ServerResponse";
import { replacePathAndQueryVariables } from "./Utils";

export default function BuildSDK(apiBaseUrl: string) { return {
	users: {
		delete: (args: { 
		id: string,
}):  Promise<ServerResponse<void, UserTypes.ErrorFormat>> => 
	fetch(
		replacePathAndQueryVariables(`${apiBaseUrl}/api/users/api/users/{id}`, args, undefined),
		{
			method: "DELETE",
		}
	)
		.then(res => res.json())
,
		create: (args: { 
	} & UserTypes.CreateUserModel
):  Promise<ServerResponse<UserTypes.User, UserTypes.ErrorFormat>> => 
	fetch(
		replacePathAndQueryVariables(`${apiBaseUrl}/api/users/api/users/`, undefined, undefined),
		{
			method: "POST",
			headers: {"Content-Type": "application/json"},
			body: JSON.stringify(args)
		}
	)
		.then(res => res.json())
,
		get: (args: { 
		id: string,
}):  Promise<ServerResponse<UserTypes.User, UserTypes.ErrorFormat>> => 
	fetch(
		replacePathAndQueryVariables(`${apiBaseUrl}/api/users/api/users/{id}`, args, undefined),
		{
			method: "GET",
		}
	)
		.then(res => res.json())
,
	},
}}
