import BuildSDK from "./api-sdk/sdk";
import { ErrorFormat, User } from "./api-sdk/UserTypes";

const sdk = BuildSDK("http://localhost:8080");

function displayError(error: ErrorFormat) {
  console.log(error);
}

(async () => {
  try {
    // CREATE
    const createUserRes = await sdk.users.create({ name: "Robert" });

    if (createUserRes.error) {
      displayError(createUserRes.error);
      return;
    }

    const userId: string = createUserRes.data.id;
    console.log("User Created:", userId);

    // GET
    const getUserRes = await sdk.users.get({ id: userId });

    if (getUserRes.error) {
      displayError(getUserRes.error);
      return;
    }

    const userAfterGet = getUserRes.data;
    console.log("User Get:", userAfterGet);

    // DELETE
    const deleteUserRes = await sdk.users.delete({ id: userId });

    if (deleteUserRes.error) {
      displayError(deleteUserRes.error);
      return;
    }

    console.log("Delete success");

    // GET after delete
    const getUserAfterDeletedRes = await sdk.users.get({ id: userId });

    if (getUserAfterDeletedRes.error) {
      displayError(getUserAfterDeletedRes.error);
      return;
    }

    console.log("Something is wrong", getUserRes.data);
  } catch (err) {
    console.log("Unexpected Error", err);
  }
})();
