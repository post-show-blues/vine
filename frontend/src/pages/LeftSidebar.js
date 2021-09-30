/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faUserFriends, faBell, faUserCircle, faSearch, faPlus } from '@fortawesome/free-solid-svg-icons'
import {navBlackHeight} from './constant';
const LeftSidebar = () => {

  const sidebarStyle = css`
    width: 92px;
    height: 100vh + ${navBlackHeight}px;
    background-color: #292828;
    padding: 40px 0;
    position: relative;
    &::after {
      content: '';
      top:0;
      left:0;
      position: absolute;
      box-shadow: -4px 0 10px rgba(0,0,0,0.25) inset; 
      width:100%;
      height:100%;
    }
  `

  return (
    <>
      <div css={sidebarStyle} className="flex flex-col items-center">
        <FontAwesomeIcon icon={faUserCircle} className="text-3xl text-white mb-11" />
        <FontAwesomeIcon icon={faBell} className="text-3xl text-white mb-11" />
        <FontAwesomeIcon icon={faUserFriends} className="text-3xl text-white mb-11" />
        <FontAwesomeIcon icon={faSearch} className="text-3xl text-white mb-11" />
        <FontAwesomeIcon icon={faPlus} className="text-3xl text-white mb-11" />
      </div>
    </>
  );
}

export default LeftSidebar;
