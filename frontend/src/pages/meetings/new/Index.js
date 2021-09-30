/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import LeftSidebar from "../../LeftSidebar"
import NavBlack from "../../NavBlack"
import Input from "../Input";
import InputImg from "../InputImg";
import { meetingsContentHeight, meetingsInputHeight } from "../../constant";
import Button from "../Button";
import Textarea from "../Textarea";

export const Index = () => {
  return (
    <div className="flex w-full">
      <LeftSidebar />
      <div className="flex flex-col w-full">
        <NavBlack />
        <div className="grid h-full grid-cols-12 bg-black" css={css`height: ${meetingsContentHeight}px;`} >
          <div className="flex flex-col items-center justify-between col-span-7 px-8 py-12" css={css`max-height: 955px;`}>
            <span className="font-bold text-white" css={css`font-size: 2.25rem;`}>Create an activity room</span>
            <Input name="DATE" type="date" height={meetingsInputHeight} />
            <Input name="DUE DATE" type="date" height={meetingsInputHeight} />
            <Input name="TITLE" type="text" height={meetingsInputHeight} />
            <Input name="INTEREST" type="date" height={meetingsInputHeight} />
            <Input name="LOCATION" type="date" height={meetingsInputHeight} />
            <Input name="CHAT ROOM" type="text" height={meetingsInputHeight} />
            <Textarea name="COMMENTS" type="text" height={meetingsInputHeight + 166} />
          </div>
          <div className="flex flex-col col-span-5 pl-8 pr-12 justify-evenly">
            <InputImg />
            <InputImg />
            <div className="flex mt-16 justify-evenly">
              <Button name="DRAFTS" />
              <Button name="UPLOAD" />
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}