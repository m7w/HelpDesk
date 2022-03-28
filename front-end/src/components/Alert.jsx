import IconButton from "@material-ui/core/IconButton";
import CloseIcon from "@material-ui/icons/Close";

function Alert(props) {
  const handleCloseAlert = () => {
    props.onClose("");
  };

  return (
    <div className="form__auth-alert">
      { props.message }
      <IconButton aria-label="close" onClick={handleCloseAlert}>
        <CloseIcon />
      </IconButton>
    </div>
  );
}

export default Alert;
