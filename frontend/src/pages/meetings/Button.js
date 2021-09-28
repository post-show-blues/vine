/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { isNull } from 'lodash';
import { innerShadow } from "../constant";

const Button = ({name, icon}) => {

  const buttonStyle = css`
    width: 208px;
    height: 54px;
    font-weight: bold;
    font-size: 1.5rem;
    background-color: #c4c4c4;
    box-shadow: ${innerShadow};

  `
  
  return (
    <button className="flex items-center justify-between" css={buttonStyle}>
      {!isNull(icon) ?
        <FontAwesomeIcon icon={icon} /> :
        null
      }
      <span className="mx-auto">{name}</span>
    </button>
  )
}

export default Button;