/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";

export const CardIcon = ({ icon, value, alt }) => {

  const textStyle = css`
    color: white;
    font-weight: 500;
    font-size: 10px;
  `

  const iconStyle = css`
    width: 20px;
    height: 20px;
    margin-right: 1rem;
  `

  return (
    <div className="flex items-center">
      <img src={icon} alt={alt} css={iconStyle} />
      <span css={textStyle}>{value}</span>
    </div>
  )
}