/** @jsxImportSource @emotion/react */
import {css} from "@emotion/react";

const LeftSidebar = () => {

  const sidebarStyle = css`
    width: 92px;
    
    color: red;
    background-color: #292828;
  `

  return (
    <div >
      <div css={sidebarStyle}>안녕!</div>
    </div>
  );
}

export default LeftSidebar;
