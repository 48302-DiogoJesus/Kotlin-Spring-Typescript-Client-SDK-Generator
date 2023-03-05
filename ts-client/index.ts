import BuildSDK from "./api-sdk/sdk";
import { ErrorFormat } from "./api-sdk/UserTypes";

const sdk = BuildSDK("http://localhost:8080");

function displayError(error: ErrorFormat) {
  console.log(error);
}

(async () => {
  try {
    // CREATE
    const createUserRes = await sdk.USERS.create({ name: "Robert" });

    if (!createUserRes.isSuccess) {
      displayError(createUserRes.error);
      return;
    }

    const userAfterCreated = createUserRes.data;
    console.log("User Created:", userAfterCreated);

    // GET
    const getUserRes = await sdk.USERS.get({ id: userAfterCreated.id });

    if (!getUserRes.isSuccess) {
      displayError(getUserRes.error);
      return;
    }

    const userAfterGet = getUserRes.data;
    console.log("User Get:", userAfterGet);

    // DELETE
    const deleteUserRes = await sdk.USERS.delete({ id: userAfterGet.id });

    if (!deleteUserRes.isSuccess) {
      displayError(deleteUserRes.error);
      return;
    }

    console.log("Delete success");

    // GET after delete
    const getUserAfterDeletedRes = await sdk.USERS.get({
      id: userAfterGet.id,
    });

    if (!getUserAfterDeletedRes.isSuccess) {
      displayError(getUserAfterDeletedRes.error);
      return;
    }

    console.log("Something is wrong", getUserRes.data);
  } catch (err) {
    console.log("Unexpected Error", err);
  }
})();
