/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { isNull } from 'lodash';
import { innerShadow } from "../constant";

const Button = ({name, icon}) => {

  const buttonStyle = css`
    width: 208px;
    height: 54px;
    font-weight: 700;
    font-size: 1.5rem;
    background-color: #c4c4c4;
    box-shadow: ${innerShadow};
    display: flex;
    align-items: center;
    justify-content: center;
  `
  
  return (
    <button className={isNull(icon) ? null : "ml-auto"} css={buttonStyle}>
      {!isNull(icon) ?
        <FontAwesomeIcon icon={icon} className="mr-4" /> :
        null
      }
      <span >{name}</span>
    </button>
  )
}

export default Button;