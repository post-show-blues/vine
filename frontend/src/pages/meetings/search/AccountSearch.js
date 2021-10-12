/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import SectionTitle from "../SectionTitle";
import AccountSearchCard from "./AccountSearchCard";

const AccountSearch = () => {
  const centerLine = css`
    border-bottom: 1px solid #FFFFFF;
    margin-left: 2rem;
    width: 100%;
  `

  return (
    <div className="flex flex-col pl-4 pr-8 mt-24">
      <div className="flex items-center">
        <SectionTitle text="ABOUT 15 RESULTS" />
        <div css={centerLine}></div>
      </div>
      {/* 루프 돌리기 */}
      <AccountSearchCard profileImg="" name="" age="" followers="" followings="" />
      <AccountSearchCard profileImg="" name="" age="" followers="" followings="" />
      <AccountSearchCard profileImg="" name="" age="" followers="" followings="" />
    </div>
  )
}

export default AccountSearch;