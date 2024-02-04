import axios from "axios";
import base64url from "base64url";
import { useRouter } from "next/router";

export const useWebAuthn = () => {
  const apiHost = process.env.NEXT_PUBLIC_API_HOST;
  const router = useRouter();

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