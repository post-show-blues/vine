/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { cardShadow, innerShadow } from "../constant";
import ButtonCard from "./ButtonCard";
import samplePerson from '../../assets/images/samplePerson.png';

const Participation = ({ status }) => {
  const cardStyle = css`
    margin-top: 61px;
    height: 300px;
    box-shadow: ${cardShadow};
    padding: 47px 28px 28px 66px;

    .img-block {
      width: 327px;
      height: 191px;
      display: flex;
      align-items: center;
      position: relative;
      
      img {
        width: 191px;
        height: 191px;
        object-fit: cover;
        border-radius: 50%;
        z-index: 3;
        box-shadow: ${innerShadow};
        filter: ${(status !== "continue") ? `grayscale(1)` : 0};
      }
      &::after {
          z-index: 2;
          background-position: center;
          background-repeat: no-repeat;
          content: '';
          background-image: url(${samplePerson});
          width: 191px;
          height: 191px;
          top: 0;
          position: absolute;
          left: 136px;
          background-size: cover;
          border-radius: 50%;
          box-shadow: ${innerShadow};
          filter: ${(status !== "continue") ? `grayscale(1)` : 0};
        }
    }

    .card-content {
      display: flex;
      flex-direction: column;
      color: white;
      font-weight: 700;
    }

  `
  return (
    <div className="flex w-full" css={cardStyle}>
      <div className="img-block">
        <img src={samplePerson} alt="" />
      </div>
      <div className="mx-8 card-content">
        <span className="mb-6 text-4xl">TITLE OF ACTIVITY</span>
        <span className="text-2xl">INFORMATION OF THE ACTIVITY</span>
      </div>

      {(status === "continue") &&
        <div className="flex mt-auto">
          <ButtonCard text="PARTICIPANTS" data="2/4" color="white" />
          <ButtonCard text="DUE-DATE" data="D-1" color="#A0FF94" />
        </div>
      }

      {(status === "accomplished") &&
        <span css={css`font-size: 36px; color: #A0FF94; margin: auto 0 0 auto; font-weight: 700;`}>ACCOMPLISHED :)</span>
      }

      {(status === "canceled") &&
        <span css={css`font-size: 36px; color: #B23030; margin: auto 0 0 auto; font-weight: 700;`}>CANCELED :(</span>
      }


    </div>
  )
}

export default Participation;