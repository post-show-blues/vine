/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { samplePerson } from "../../../assets/images/images"

const AccountSearchCard = ({ profileImg, name, followers, followings }) => {
  const cardStyle = css`
    border-radius: 150px;
    width: 100%;
    height: 222px;
    border: 1px solid #FFFFFF;
    margin: 65px 0 30px 0;
    display: flex;
    align-items: center;
    color: white;
    font-size: 35px;
    font-weight: 700;

    img {
      border-radius: 50%;
      object-fit: cover;
      width: 222px;
      height: 222px;
    }
  `

  return (
    <div css={cardStyle}>
      <img src={samplePerson} alt="프로필사진" />
      <div className="flex flex-col ml-12">
        <div className="flex">
          <span>LAURA KIM,&nbsp;</span>
          <span>29</span>
        </div>
        <div className="flex">
          <span className="mr-8">120 FOLLOWERS</span>
          <span>181 FOLLOWINGS</span>
        </div>
      </div>
    </div>
  );
}

export default AccountSearchCard;