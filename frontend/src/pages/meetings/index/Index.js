/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import LeftSidebar from "../../LeftSidebar"
import NavBlack from "../../NavBlack"
import SectionTitle from "../SectionTitle";
import { defaultContentPadding } from "../../constant";
import MainImg from "./MainImg";
import { mainImg } from "../../../assets/images/images";
import CardList from "../CardList";

export const Index = () => {
  return (
    <div className="flex w-full">
      <LeftSidebar />
      <div className="flex flex-col w-full">
        <NavBlack />
        <div className="flex flex-col" css={css`${defaultContentPadding}`}>
          <SectionTitle text="WHAT IS NEW" />
          <MainImg url={mainImg} alt="대문이미지" />
          <SectionTitle text="FOLLOWING" marginTop="1rem" />
          <CardList />
        </div>
      </div>
    </div>
  )
}
