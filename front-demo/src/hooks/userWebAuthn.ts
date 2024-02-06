import axios from "axios";
import base64url from "base64url";
import { useRouter } from "next/router";
import { toast } from "react-toastify";

export const useWebAuthn = () => {
  const apiHost = process.env.NEXT_PUBLIC_API_HOST;
  const router = useRouter();

  const startRegistration = async (attachment: string) => {
    return await axios(`${apiHost}/api/v1/webauthn/registration/start`, {
      params: {
        authenticatorAttachment: attachment,
      },
      withCredentials: true,
    })
      .then((response) => {
        return response.data;
      })
      .catch((error) => {
        console.log(error);
        toast.error("Error starting registration");
      });
  };

  const createCredentials = async (options: any) => {
    options.user.id = bufferDecode(options.user.id);
    options.challenge = bufferDecode(options.challenge);

    return await navigator.credentials
      .create({
        publicKey: options,
      })
      .then((response) => {
        if (!response) {
          throw new Error("No response from navigator.credentials.create");
        }
        return response;
      })
      .catch((error) => {
        console.log(error);
        toast.error("Error creating");
      });
  };

  const registerCredentials = async (flowId: string, credentials: any) => {
    await axios(`${apiHost}/api/v1/webauthn/registration/finish`, {
      method: "POST",
      withCredentials: true,
      data: {
        flowId: flowId,
        id: credentials.id,
        rawId: base64url.encode(credentials.rawId),
        type: credentials.type,
        response: {
          attestationObject: base64url.encode(
            credentials.response.attestationObject
          ),
          clientDataJSON: base64url.encode(credentials.response.clientDataJSON),
        },
      },
    })
      .then((response) => {
        console.log(response.data);
        toast.success("Registered successfully");
      })
      .catch((error) => {
        console.log(error.response.data);
        toast.error("Error registering");
      });
  };

  const getCredentials = async (options: any) => {
    options.challenge = bufferDecode(options.challenge);
    for (let cred of options.allowCredentials) {
      cred.id = bufferDecode(cred.id);
    }

    return await navigator.credentials
      .get({
        publicKey: options,
      })
      .then((response) => {
        console.log(response);
        return response;
      })
      .catch((error) => {
        console.log(error);
        toast.error("Error getting credentials");
      });
  };

  const postCredentials = async (flowId: string, credentials: any) => {
    console.log(credentials);
    await axios(`${apiHost}/api/v1/webauthn/login`, {
      method: "POST",
      withCredentials: true,
      data: {
        flowId: flowId,
        id: credentials.id,
        rawId: base64url.encode(credentials.rawId),
        type: credentials.type,
        response: {
          authenticatorData: base64url.encode(
            credentials.response.authenticatorData
          ),
          clientDataJSON: base64url.encode(credentials.response.clientDataJSON),
          signature: base64url.encode(credentials.response.signature),
          userHandle: credentials.response.userHandle
            ? base64url.encode(credentials.response.userHandle)
            : null,
        },
      },
    })
      .then((response) => {
        console.log(response.data);
        router.push("/");
      })
      .catch((error) => {
        console.log(error.response.data);
        toast.error("Error logging in");
      });
  };

  return {
    startRegistration,
    createCredentials,
    registerCredentials,
    getCredentials,
    postCredentials,
  };
};

function bufferDecode(value: string) {
  return Uint8Array.from(
    atob(value.replace(/-/g, "+").replace(/_/g, "/")),
    (c) => c.charCodeAt(0)
  );
}
