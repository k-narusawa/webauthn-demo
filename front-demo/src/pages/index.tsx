import axios from "axios";
import { useRouter } from "next/router";
import base64url from "base64url";
import { useEffect, useState } from "react";

const HomePage = () => {
  const [userinfo, setUserInfo] = useState<any>();
  const router = useRouter();
  const apiHost = process.env.NEXT_PUBLIC_API_HOST;

  useEffect(() => {
    const fetchUserInfo = async () => {
      await axios(`${apiHost}/api/v1/userinfo`, {
        withCredentials: true,
      })
        .then((response) => {
          setUserInfo(response.data);
        })
        .catch((error) => {
          console.log(error.response.data);
          router.push("/login");
        });
    };
    fetchUserInfo();
  }, [apiHost, router]);

  const handleRegister = async () => {
    await axios(`${apiHost}/api/v1/webauthn/registration/start`, {
      withCredentials: true,
    })
      .then((response) => {
        console.log(response.data);
        createCredential(response.data);
      })
      .catch((error) => {
        console.log(error.response.data);
      });
  };

  const createCredential = async (options: any) => {
    options.user.id = bufferDecode(options.user.id);
    options.challenge = bufferDecode(options.challenge);

    console.log(options);
    await navigator.credentials
      .create({
        publicKey: options,
      })
      .then((response) => {
        console.log(response);
        registerCredentials(options.flowId, response);
      })
      .catch((error) => {
        console.log(error);
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
        createCredential(response.data);
      })
      .catch((error) => {
        console.log(error.response.data);
      });
  };

  const handleLogout = async () => {
    await axios(`${apiHost}/api/v1/logout`, {
      method: "POST",
      withCredentials: true,
    })
      .then(function (response) {
        console.log(response.data);
        router.push("/login");
      })
      .catch(function (error) {
        console.log(error.response.data);
      });
  };

  function bufferDecode(value: string) {
    return Uint8Array.from(
      atob(value.replace(/-/g, "+").replace(/_/g, "/")),
      (c) => c.charCodeAt(0)
    );
  }

  return (
    <>
      <h1>Home</h1>
      <label>Username: {userinfo?.username}</label>
      <br />
      <button
        onClick={handleRegister}
        type="button"
        className="focus:outline-none text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-900"
      >
        Registration
      </button>
      <button
        onClick={handleLogout}
        type="button"
        className="focus:outline-none text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-900"
      >
        Logout
      </button>
    </>
  );
};

export async function getStaticProps() {
  return {
    props: {},
  };
}

export default HomePage;
