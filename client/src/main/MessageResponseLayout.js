import React, 
{ useState } 
from 'react'

export default function MessageResponse(props){
    const [visible, setVisible] = useState(0);

    let setNotVisible = ()=>{
        setVisible(false);
    }

    function getErrorMessage(errorBody){
        return(
            <div class="ui error message" >
                <i class="close icon" onClick={setNotVisible}></i>
                <div class="header">
                    Error!
                </div>
                errorBody
            </div>
      )
    }

    function getSuccessMessage(successBody){
            return(
                <div class="ui success message">
                    <i class="close icon" onClick={setNotVisible}></i>
                    <div class="header">
                        Success!
                    </div>
                    successBody
                </div>
        )
        }
    if(visible === false){
        return null
    }
    else if(props.errorResponse.error === true){
        return getErrorMessage(props.errorResponse.errorBody);
    }
    else if(props.normalResponse.success === true){
        return getSuccessMessage(props.errorResponse.errorBody);
    }
    else
        return null;
}