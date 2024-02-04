import axios from "axios";
import base64url from "base64url";
import { useRouter } from "next/router";

export const useWebAuthn = () => {
  const apiHost = process.env.NEXT_PUBLIC_API_HOST;
  const router = useRouter();

  const startRegistration = async () => {
    return await axios(`${apiHost}/api/v1/webauthn/registration/start`, {
      withCredentials: true,
    })
      .then((response) => {
        return response.data;
      })
      .catch((error) => {
        console.log(error.response.data);
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
        console.log(response);
        if(!response){
          throw new Error('No response from navigator.credentials.create');
        }
        return response;
      })
      .catch((error) => {
        throw new Error(error);
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
      })
      .catch((error) => {
        throw new Error(error.response.data);
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
      });
  };

  const postCredentials = async (flowId: string, credentials: any) => {
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
      });
  };

  return {
    startRegistration,
    createCredentials,
    registerCredentials,
    getCredentials,
    postCredentials
  };
}

function bufferDecode(value: string) {
  return Uint8Array.from(
    atob(value.replace(/-/g, "+").replace(/_/g, "/")),
    (c) => c.charCodeAt(0)
  );
}