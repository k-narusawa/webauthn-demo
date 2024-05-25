import axios from "axios";
import { useRouter } from "next/router";
import { useEffect, useState } from "react";
import { useWebAuthn } from "@/hooks/userWebAuthn";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import ProfileCard from "@/components/pages/top/ProfileCard";
import { Button } from "@/components/commons/Button";
import { PasskeyCard } from "@/components/pages/top/PasskeyCard";

const HomePage = () => {
  const [userinfo, setUserInfo] = useState<any>();
  const [userCredentials, setUserCredentials] = useState<any>();
  const router = useRouter();
  const { getOptions, createCredential, registerCredential } =
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
    const fetchUserCredentials = async () => {
      await axios(`${apiHost}/api/v1/users/credentials`, {
        withCredentials: true,
      })
        .then((response) => {
          console.log(response.data);
          setUserCredentials(response.data);
        })
        .catch((error) => {
          console.log(error.response.data);
        });
    };

    fetchUserInfo();
    fetchUserCredentials();
  }, [apiHost, router]);

  const handleRegistration = async () => {
    const startResponse = await getOptions();
    const credentials = await createCredential(startResponse);
    await registerCredential(startResponse.challenge, credentials);
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
      <div className="pt-10 px-10">
        <ToastContainer />
        <ProfileCard username={userinfo?.username} />

        <div className="pt-10"/>

        <PasskeyCard passkeys={null} onRegister={handleRegistration} onDelete={() => {}} />
      
        <div className="pt-10"/>
        
        <div className="flex justify-center">
          <div className="w-48">
            <Button
              variant="danger"
              onClick={handleLogout}
              type="button"
            >
              Logout
            </Button>
          </div>
        </div>
      </div>
    </>
  );
};

export async function getStaticProps() {
  return {
    props: {},
  };
}

export default HomePage;
