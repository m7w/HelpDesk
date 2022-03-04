import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import MenuItem from '@material-ui/core/MenuItem';
import Menu from '@material-ui/core/Menu';

const styles = theme => ({
  root: {
    width: '100%',
    maxWidth: 360,
    backgroundColor: theme.palette.background.paper,
  },
});

class DropDown extends React.Component {
  state = {
    anchorEl: null,
    selectedIndex: 1,
  };

  handleClickListItem = event => {
    this.setState({ anchorEl: event.currentTarget });
  };

  handleMenuItemClick = (index, column) => {
    this.setState({ selectedIndex: index, anchorEl: null });
    if (typeof this.props.onSelect === 'function') {
      console.log("In handleMenuItemClick");
      this.props.onSelect(column);
    }
  };

  handleClose = () => {
    this.setState({ anchorEl: null });
  };

  render() {
    const { handleClickListItem, handleMenuItemClick, handleClose } = this;
    const { options } = this.props;
    const { anchorEl, selectedIndex } = this.state;

    return (
      <div style={{width: "120px"}}>
        <List component="nav">
          <ListItem
            button
            aria-haspopup="true"
            aria-controls="lock-menu"
            aria-label="Search by:"
            onClick={handleClickListItem}
          >
            <ListItemText
              primary="Search by:"
              secondary={options[selectedIndex].label}
            />
          </ListItem>
        </List>
        <Menu
          id="lock-menu"
          anchorEl={anchorEl}
          open={Boolean(anchorEl)}
          onClose={handleClose}
        >
          {options.map((option, index) => (
            <MenuItem
              key={option.label}
              disabled={index === selectedIndex}
              selected={index === selectedIndex}
              onClick={() => handleMenuItemClick(index, option.db_name)}
            >
              {option.label}
            </MenuItem>
          ))}
        </Menu>
      </div>
    );
  }
}

DropDown.propTypes = {
  options: PropTypes.array.isRequired,
};

export default withStyles(styles)(DropDown);
