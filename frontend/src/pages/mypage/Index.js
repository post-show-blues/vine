/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { useRef, useEffect, useState } from 'react';
import LeftSidebar from "../LeftSidebar"
import NavBlack from "../NavBlack"
import Participation from "./Participation";
import Profile from "./Profile";

export const Index = () => {
  const titleReference = useRef(null);
  const [hrHeight, setHrHeight] = useState(0);
  const hrPl = 47;

  const cardListStyle = css`
    background-color: black;
    width: 100%;
    padding: 1rem ${hrPl}px 0 ${hrPl}px;

    .title {
      color: #A0FF94;
      font-size: 30px;
      font-weight: 700;
    }
  `
  

  const hrStyle = css`
    border-top: 1px solid #404040;
    position:relative;
    &::after {
      left: ${hrPl}px;
      position:absolute;
      top: -6px;
      content: '';
      border-radius: 10px;
      height: 11px;
      width: ${hrHeight}px;
      background-color: #A0FF94;
    }
  `

  useEffect(() => {
    setHrHeight(titleReference.current.offsetWidth);
  }, [])

  return (
    <div className="flex w-full">
      <LeftSidebar />
      <div className="flex flex-col w-full h-full bg-black">
        <NavBlack />
        <Profile />
        <div css={hrStyle} />
        <div css={cardListStyle}>
          <span className="title" ref={titleReference}>ALL PARTICIPATED ACTIVITES</span>
          <Participation status="continue"/>
          <Participation status="continue" />
          <Participation status="accomplished" />
          <Participation status="canceled" />
        </div>
      </div>
    </div>
  )
}
