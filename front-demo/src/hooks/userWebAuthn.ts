import axios from "axios";
import base64url from "base64url";
import { useRouter } from "next/router";
import { toast } from "react-toastify";

export const useWebAuthn = () => {
  const apiHost = process.env.NEXT_PUBLIC_API_HOST;
  const router = useRouter();

  const getOptions = async () => {
    return await axios(`${apiHost}/v1/webauthn/registration/options`, {
      withCredentials: true,
    })
      .then((response) => {
        return response.data;
      })
      .catch((error) => {
        toast.error("Error starting registration");
      });
  };

  const createCredential = async (options: any) => {
    console.log(options);
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
        toast.error("Error creating");
      });
  };

  const registerCredential = async (challenge: string, credentials: any) => {
    await axios(`${apiHost}/v1/webauthn/registration/results`, {
      method: "POST",
      withCredentials: true,
      data: {
        challenge: base64url.encode(challenge),
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

  const getAuthOptions = async (options: any) => {
    options.challenge = bufferDecode(options.challenge);
    for (let cred of options.allowCredentials) {
      cred.id = bufferDecode(cred.id);
    }

    console.log(options);

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

  const postResults = async (challenge: string, credentials: any) => {
    await axios(`${apiHost}/v1/webauthn/authentication`, {
      method: "POST",
      withCredentials: true,
      data: {
        challenge: base64url.encode(challenge),
        id: credentials.id,
        rawId: base64url.encode(credentials.rawId),
        type: credentials.type,
        response: {
          authenticatorData: base64url.encode(
            credentials.response.authenticatorData
          ),
          clientDataJSON: base64url.encode(credentials.response.clientDataJSON),
          signature: base64url.encode(credentials.response.signature),
          userHandle: base64url.encode(credentials.response.userHandle),
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
    getOptions,
    createCredential,
    registerCredential,
    getAuthOptions,
    postResults,
  };
};

function bufferDecode(value: string) {
  return Uint8Array.from(
    atob(value.replace(/-/g, "+").replace(/_/g, "/")),
    (c) => c.charCodeAt(0)
  );
}
