/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { innerShadow } from "../constant";
import samplePerson from "../../assets/images/samplePerson.png";
import { mainImg } from '../../assets/images/images';
import { CardIcon } from "./CardIcon";
import { starActive, starDeactvie, likeActive, likeDeactive, comment } from "../../assets/icon/Icon";


const Card = () => {
  const cardStyle = css`
    box-shadow: ${innerShadow};
    width: 100%;
    border-radius: 16px;
    display: flex;
    flex-direction: column;
    padding: 25px;
    background-color: rgba(66, 65, 65, 0.4);
    height: 541px;
  `

  const hostImg = css`
    width: 55px;
    height: 55px;
    border-radius: 50%;
    object-fit: cover;
  `

  const hostName = css`
    color: white;
    font-size: 12px;
    font-weight: 700;
  `

  const cardCreatedAt = css`
    color: #79E48B;
    font-size: 11.7px;
    font-weight: 700;
  `

  const cardPersonnel = css`
    color: #A0FF94;
    border-radius: 5px;
    border: 1px solid white;
    display: flex;
    justify-content: center;
    align-items: center;
    width: fit-content;
    padding: 0 0.25rem;
  `

  const placeImg = css`
    width: 100%;
    height: 180px;
    border-radius: 16px;
    margin: 1.5rem 0;
  `

  const cardTitle = css`
    color: white;
    font-size: 12px;
    font-weight: 700;
    line-height: 15.6px;
    margin-bottom: 1rem;
    
  `

  const cardContent = css`
    color: #C4C4C4;
    font-size: 10px;
    line-height: 13px;
    font-weight: 500;
  `

  return (
    <div css={cardStyle}>
      <div className="flex items-center justify-between">
        <img src={samplePerson} alt="방장프로필" css={hostImg} />
        <div className="flex flex-col ml-6 mr-auto">
          <span css={hostName}>tkdydwk dlfam</span>
          <span css={cardCreatedAt}>2일전</span>
        </div>
        <div css={cardPersonnel}>2/4</div>
      </div>
      <img src={mainImg} alt="" css={placeImg} />
      <span css={cardTitle}>여기는 제목. 캠핑할 사람 모집하고 있어요.</span>
      <span css={cardContent}>Lorem ipsum dolor sit, amet consectetur adipisicing elit. Exercitationem nobis natus quidem vero deserunt voluptates, sapiente accusamus quis vel. Sed labore laudantium, optio asperiores voluptas a expedita aspernatur? Soluta, esse.</span>
      <div className="flex items-center justify-between mt-auto">
        <CardIcon icon={likeActive} value="20" alt="좋아요" />
        <CardIcon icon={comment} value="15" alt="댓글" />
        <CardIcon icon={starActive} value="10" alt="북마크" />
      </div>
    </div>
  )
}

export default Card;