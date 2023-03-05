import BuildSDK from "./api-sdk/sdk";
import { ErrorFormat } from "./api-sdk/UserTypes";

/**
 * Same as:
 * const sdk = BuildSDK("http://localhost:8080")
 * const userEndpoints = sdk.users
 */
const { users: userEndpoints } = BuildSDK("http://localhost:8080");

(async () => {
  try {
    // CREATE
    const createUserRes = await userEndpoints.create({ name: "Robert" });

    if (createUserRes.error) {
      throw createUserRes.error;
    }

    const userId: string = createUserRes.data.id;
    console.log("User Created:", userId);

    // GET
    const getUserRes = await userEndpoints.get({ id: userId });

    if (getUserRes.error) {
      throw getUserRes.error;
    }

    const userAfterGet = getUserRes.data;
    console.log("User Get:", userAfterGet);

    // DELETE
    const deleteUserRes = await userEndpoints.delete({ id: userId });

    if (deleteUserRes.error) {
      throw deleteUserRes.error;
    }

    console.log("Delete success");

    // GET after delete
    const getUserAfterDeletedRes = await userEndpoints.get({ id: userId });

    if (getUserAfterDeletedRes.error) {
      throw getUserAfterDeletedRes.error;
    }

    console.log("Something is wrong", getUserRes.data);
  } catch (err) {
    console.log("Error", err);
  }
})();
