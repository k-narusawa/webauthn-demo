import { Card } from "@/components/commons/Card";
import { HorizontalLine } from "@/components/commons/HorizontalLine";

type Props = {
  username: string;
}

const ProfileCard: React.FC<Props> = ({username}) => {
  return (
    <Card>
      <div className="p-4 flex justify-center text-2xl font-semi-bold">
        Profile
      </div>

      <HorizontalLine />

      <div className="grid grid-cols-6 py-4 px-8">
        <div className="col-start-1">
          <span className="text-gray-500">Username</span>
        </div>
        <div className="col-start-5 whitespace-nowrap">{username}</div>
      </div>
    </Card>
  );
}

export default ProfileCard;