import axios from "axios";
import { useRouter } from "next/router";
import { useEffect, useState } from "react";
import { useWebAuthn } from "@/hooks/userWebAuthn";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const HomePage = () => {
  const [userinfo, setUserInfo] = useState<any>();
  const router = useRouter();
  const { startRegistration, createCredentials, registerCredentials } =
    useWebAuthn();
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

  const handleRegister = async (attachment: string) => {
    const startResponse = await startRegistration(attachment);
    const credentials = await createCredentials(startResponse);
    await registerCredentials(startResponse.flowId, credentials);
  };

  const handleLogout = async () => {
    await axios(`${apiHost}/api/v1/logout`, {
      method: "POST",
      withCredentials: true,
    })
      .then(() => {
        router.push("/login");
      })
      .catch((error) => {
        console.log(error.response.data);
        toast.error("Failed to logout");
      });
  };

  return (
    <>
      <h1>Home</h1>
      <ToastContainer />
      <label>Username: {userinfo?.username}</label>
      <br />
      <button
        onClick={() => {
          handleRegister("cross-platform");
        }}
        type="button"
        className="focus:outline-none text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-900"
      >
        CROSS_PLATFORM Registration
      </button>
      <br />
      <button
        onClick={() => {
          handleRegister("platform");
        }}
        type="button"
        className="focus:outline-none text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-900"
      >
        PLATFORM Registration
      </button>
      <br />
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
