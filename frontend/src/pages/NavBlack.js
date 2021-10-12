/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import logo from "../assets/icon/logo.svg";
import {navBlackHeight} from './constant';
const NavBlack = () => {
  return (
    <div className="items-center justify-center w-full bg-black" css={css`height: ${navBlackHeight}px;`}>
      <img src={logo} alt="로고" className="mx-auto" />
    </div>
  );
}

export default NavBlack;