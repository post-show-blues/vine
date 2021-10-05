/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { useState, useRef, useEffect } from 'react';
import LeftSidebar from "../../LeftSidebar"
import NavBlack from "../../NavBlack"
import SectionTitle from "../SectionTitle";
import { defaultContentPadding } from "../../constant";
import MainImg from "../index/MainImg";
import { mainImg } from "../../../assets/images/images";
import CardList from "../CardList";
import SearchBar from "./SearchBar";
import SearchFormParent from "./SearchFormParent";

export const Index = () => {
  const [isInputFocus, setIsInputFocus] = useState(false);

  const onInputClick = () => {
    setIsInputFocus(true);
  }

  return (
    <div className="flex w-full">
      <LeftSidebar />
      <div className="flex flex-col w-full">
        <NavBlack />
        <div className="flex flex-col" css={css`${defaultContentPadding}`}>
          <SectionTitle text="WHAT IS NEW" />
          <MainImg url={mainImg} alt="대문이미지" />
          <SearchBar onInputClick={onInputClick}/>

          {!isInputFocus ?
            <>
              <SectionTitle text="TOP VIEW" marginTop="1rem" />
              <CardList />
            </> : 
            <SearchFormParent />
          }


        </div>
      </div>
    </div>
  )
}
