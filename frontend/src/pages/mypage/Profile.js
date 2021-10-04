/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import samplePerson from '../../assets/images/samplePerson.png';
import gear from '../../assets/images/gear.svg';
import AddSns from "./AddSns";
import { faFacebook, faInstagram } from '@fortawesome/free-brands-svg-icons'
import { innerShadow } from "../constant";

const Profile = () => {

  const profileStyle = css`
    padding: 40px 31px 35px 31px;
    .img-circle {
      margin-left: 30px;
      margin-right: 148px;
      border-radius: 50%;
      width: 305px;
      height: 305px;
      border: 1px solid white;
      position: relative;
      &::after {
        content: '';
        background-image: url(${samplePerson});
        background-size: cover;
        background-position: center;
        background-repeat: no-repeat;
        border-radius: 50%;
        text-align: center;
        top: 13px;
        left: 13px;
        position: absolute;
        width: 279px;
        height: 279px;
      }
    }

    .id {
      font-size: 36px;
    }

    .button {
      color: black;
      background-color: #c4c4c4;
      display: flex;
      align-items: center;
      padding: 0 35px;
      border-radius: 8px;
      box-shadow: ${innerShadow};
    }
 
  `

  return (
    <div className="flex" css={profileStyle}>
      <div className="flex-none rounded-full img-circle"></div>
      <div className="flex flex-col w-full font-bold text-white">
        <div className="flex mb-2 id">
          <span>ID7282</span>
          <img src={gear} alt="edit_id_button" style={{ marginLeft: "2rem" }} />
        </div>
        <span className="mb-2 text-2xl">TOTAL PARTICIPATED ACTIVITES - 15</span>
        <div className="flex mb-4">
          <span className="mr-8 text-2xl">FOLLOWERS 170</span>
          <span className="text-2xl">FOLLOWING 170</span>
        </div>
        <div className="flex mb-8">
          <AddSns text="FACEBOOK" icon={faFacebook}/>
          <AddSns text="INSTAGRAM" icon={faInstagram} />
        </div>
        <span className="mb-2 text-2xl">CHRISTINA YANG (25, &#9792;)</span>
        <span className="text-xl" style={{ fontWeight: "normal" }}>MINGLING AROUND ~ ~ </span>
        <span className="mt-auto ml-auto button">EDIT</span>
      </div>
    </div>
  )
}

export default Profile